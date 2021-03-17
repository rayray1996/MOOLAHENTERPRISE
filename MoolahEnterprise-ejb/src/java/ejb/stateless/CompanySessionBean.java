/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import com.sun.org.apache.bcel.internal.generic.DADD;
import ejb.entity.CompanyEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.ProductEntity;
import ejb.entity.RefundEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CompanyAlreadyExistException;
import util.exception.CompanyCreationException;
import util.exception.CompanyDoesNotExistException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;

/**
 *
 * @author nickg
 */
@Stateless
public class CompanySessionBean implements CompanySessionBeanLocal {

    @EJB
    private EmailSessionBeanLocal emailSessionBean;

    @EJB
    private RefundSessionBeanLocal refundSessionBean;

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @Resource
    private SessionContext sessionContext;

    private TimerService timerService;

    public CompanySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        timerService = sessionContext.getTimerService();

    }

    @Override
    public CompanyEntity createAccountForCompany(CompanyEntity newCompany) throws CompanyAlreadyExistException, UnknownPersistenceException, CompanyCreationException {
        Set<ConstraintViolation<CompanyEntity>> companyError = validator.validate(newCompany);
        if (companyError.isEmpty()) {
            try {
                em.persist(newCompany);
                em.flush();

                return newCompany;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CompanyAlreadyExistException(ex.getMessage());
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new CompanyCreationException(prepareInputDataValidationErrorsMessage(companyError));
        }
    }

    @Override
    public CompanyEntity login(String companyEmail, String password) throws CompanyDoesNotExistException, IncorrectLoginParticularsException {
        CompanyEntity currcompany = retrieveCompanyByEmail(companyEmail);
        String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + currcompany.getSalt()));
        if (currcompany.getPassword().equals(passwordHash)) {
            return currcompany;
        } else {
            throw new IncorrectLoginParticularsException("Incorrect login details provided!");
        }
    }

    @Override
    public CompanyEntity retrieveCompanyByEmail(String email) throws CompanyDoesNotExistException {
        try {
            CompanyEntity company = (CompanyEntity) em.createQuery("SELECT c FROM CompanyEntity c WHERE c.companyEmail =:coyEmail").setParameter("coyEmail", email).getSingleResult();

            company.getListOfPointOfContacts().size();
            for (MonthlyPaymentEntity monthlyPayment : company.getListOfMonthlyPayments()) {
                monthlyPayment.getListOfProductLineItems().size();
            }

            for (ProductEntity prod : company.getListOfProducts()) {
                prod.getListOfAdditionalFeatures().size();
                prod.getListOfPremium().size();
                prod.getListOfRiders().size();
            }
            return company;
        } catch (NoResultException ex) {
            throw new CompanyDoesNotExistException("Company with email " + email + " does not exist!");
        }
    }

    @Override
    public void updateCompanyInformation(CompanyEntity company) throws UnknownPersistenceException, CompanyDoesNotExistException {
        try {
            em.merge(company);
            em.flush();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CompanyDoesNotExistException(ex.getMessage());
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    public void topupCredit(CompanyEntity company, BigInteger creditAmount) throws CompanyDoesNotExistException {
        CompanyEntity companyToUpdate = retrieveCompanyByEmail(company.getCompanyEmail());
        companyToUpdate.setCreditOwned(companyToUpdate.getCreditOwned().add(creditAmount));
        companyToUpdate.setIsWarned(Boolean.FALSE);
        companyToUpdate.setIsDeactivated(true);
    }

    @Override
    public void deactivateAccount(String email) throws CompanyDoesNotExistException {

        CompanyEntity company = retrieveCompanyByEmail(email);
        company.setIsDeactivated(true);
        TimerConfig timerConfig = new TimerConfig(company, true);
        LocalDateTime expirationDate = LocalDateTime.now();
        expirationDate.plusMonths(6);
        Date expiration = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());

//        timerService.createSingleActionTimer(300000, timerConfig);
        
        //correct timerservice
        timerService.createSingleActionTimer(expiration, timerConfig);
    }

    /**
     * This method will lazy delete the company profile and all products This is
     * used in conjunction with deactivateAccount() method, where it will
     * trigger a 6 months timer
     *
     * @param timer
     */
    @Timeout
    @Override
    public void timeoutCleanUp(Timer timer) {
        CompanyEntity company = (CompanyEntity) timer.getInfo();
        System.out.println("TImeout method triggered! Company: " + company.getCompanyId());

        //COMPANY CURRENTLY DEACTIVATED, transitioning to DELETED:  initiate refund, set all products to isDeleted, set company to isDeleted
        if (company.isIsDeactivated()) {
            RefundEntity refundEntity = new RefundEntity(new GregorianCalendar(), new BigDecimal(company.getCreditOwned().intValueExact() * 0.10), company);
            company.setRefund(refundEntity);
            for (ProductEntity coyProd : company.getListOfProducts()) {
                coyProd.setIsDeleted(Boolean.TRUE);
            }
            company.setIsDeleted(true);
        } else if (company.getIsWarned()) {
            // deactivate account, set timer to 6 months - call deactivateAccount()
            company.setIsDeactivated(true);
            TimerConfig timerConfig = new TimerConfig(company, true);
            LocalDateTime expirationDate = LocalDateTime.now();
            expirationDate.plusMonths(6);
            Date expiration = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());
            
            //send email informing them of the deactivation of their account!!
            Boolean result = emailSessionBean.emailReminderAccountDeactivatedSync(company, company.getCompanyEmail());
            timerService.createSingleActionTimer(expiration, timerConfig);
        }

    }

    /**
     * This method carries out automated checks on the each company's credit
     * balance to ensure that there is no negative amount
     */
//    @Schedule(hour = "7", minute = "0", second = "0", dayOfMonth = "20", month = "*", year = "*", persistent = true)
    @Schedule(hour = "0", minute = "*/5", second = "0", dayOfMonth = "17", month = "*", year = "*", persistent = true)
    public void automatedCheckCreditBalance() {
        List<CompanyEntity> listOfCompanies = em.createQuery("SELECT coy FROM CompanyEntity coy WHERE coy.isDeleted = false").getResultList();
        for (CompanyEntity company : listOfCompanies) {
            if (company.getCreditOwned().intValueExact() <= 100 && !company.getIsWarned()) {
                Boolean result = emailSessionBean.emailCreditTopupNotificationSync(company, company.getCompanyEmail());
                company.setIsWarned(Boolean.TRUE);

                TimerConfig timerConfig = new TimerConfig(company, true);
                LocalDateTime expirationDate = LocalDateTime.now();
                expirationDate.plusWeeks(1);
                Date expiration = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());
                timerService.createSingleActionTimer(expiration, timerConfig);

                if (!result) {
                    company.setWarningMessage("We have failed to contact you via email. Please top up your credit by end of the month! ");
                }
            }
        }
    }

//    public void trackDeactivationAndDeletion()
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CompanyEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
