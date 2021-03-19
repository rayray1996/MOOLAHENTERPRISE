/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import com.sun.org.apache.bcel.internal.generic.DADD;
import ejb.Singleton.MoolahCreditConverterLocal;
import ejb.entity.CompanyEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.ProductEntity;
import ejb.entity.RefundEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.NoSuchObjectLocalException;
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
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CompanyAlreadyExistException;
import util.exception.CompanyCreationException;
import util.exception.CompanyDoesNotExistException;
import util.exception.CompanySQLConstraintException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.MonthlyPaymentNotFoundException;
import util.exception.RefundCreationException;
import util.exception.RefundErrorException;
import util.exception.RefundHasBeenTransactedException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;

/**
 *
 * @author nickg
 */
@Stateless
public class CompanySessionBean implements CompanySessionBeanLocal {

    @EJB
    private MoolahCreditConverterLocal moolahCreditConverter;

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

    }

    @PostConstruct
    public void init() {
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
                        throw new CompanyAlreadyExistException("Company already exists!");
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
            for (PaymentEntity monthlyPayment : company.getListOfPayments()) {
                if (monthlyPayment instanceof MonthlyPaymentEntity) {
                    ((MonthlyPaymentEntity) monthlyPayment).getListOfProductLineItems().size();
                }
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
    public List<CompanyEntity> retrieveAllActiveCompanies() {
        List<CompanyEntity> listOfCompanies = em.createQuery("SELECT c FROM CompanyEntity c WHERE c.isDeleted = FALSE").getResultList();
        for (CompanyEntity coy : listOfCompanies) {
            coy.getListOfProducts().size();
        }

        return listOfCompanies;
    }

    @Override
    public void updateCompanyInformation(CompanyEntity company) throws UnknownPersistenceException, CompanySQLConstraintException {
        try {
            em.merge(company);
            em.flush();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CompanySQLConstraintException(ex.getMessage());
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public void topupCredit(CompanyEntity company, BigInteger creditAmount) throws CompanyDoesNotExistException {
        CompanyEntity companyToUpdate = retrieveCompanyByEmail(company.getCompanyEmail());
        companyToUpdate.setCreditOwned(companyToUpdate.getCreditOwned().add(creditAmount));
        companyToUpdate.setIsWarned(Boolean.FALSE);
        companyToUpdate.setIsDeactivated(false);

        Collection<Timer> timers = timerService.getTimers();

        try {
            for (Timer timer : timers) {
                if (timer.getInfo() != null) {
                    CompanyEntity companyInTimer = (CompanyEntity) timer.getInfo();
                    if (companyInTimer.getCompanyId().equals(companyToUpdate.getCompanyId())) {
                        timer.cancel();
                    }
                }
            }
        } catch (NoSuchObjectLocalException ex) {
            System.out.println("Timer has been cancelled or does not exists! Msg: " + ex.getMessage());
        }

//        Timer timerToStop = timerService.
    }

    @Override
    public void deactivateAccount(String email) throws CompanyDoesNotExistException {

        CompanyEntity company = retrieveCompanyByEmail(email);
        company.setIsDeactivated(true);
        TimerConfig timerConfig = new TimerConfig(company, true);
        LocalDateTime expirationDate = LocalDateTime.now();

        //Testing code - For showcase
        expirationDate = expirationDate.plusMinutes(5);

        //Deployment code - Business Logic
//        expirationDate = expirationDate.plusMonths(6);
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
        try {
            CompanyEntity company = (CompanyEntity) timer.getInfo();
            company = retrieveCompanyByEmail(company.getCompanyEmail());
            System.err.println("TImeout method triggered! Company: " + company.getCompanyId());

            //COMPANY CURRENTLY DEACTIVATED, transitioning to DELETED:  initiate refund, set all products to isDeleted, set company to isDeleted
            if (company.isIsDeactivated()) {

                BigDecimal creditinSGD = moolahCreditConverter.convertCreditToSgd(company.getCreditOwned());
                RefundEntity refundEntity = new RefundEntity(new GregorianCalendar(), creditinSGD, company);
                refundEntity = refundSessionBean.createNewRefund(refundEntity);
                company.setRefund(refundEntity);
                for (ProductEntity coyProd : company.getListOfProducts()) {
                    coyProd.setIsDeleted(true);
                }
                System.err.println("Company: " + company.getCompanyName() + " is deleted!");
                company.setIsDeleted(true);

            } else if (company.getIsWarned()) {

                // deactivate account, set timer to 6 months - call deactivateAccount()
                company.setIsDeactivated(Boolean.TRUE);
                TimerConfig timerConfig = new TimerConfig(company, true);
                LocalDateTime expirationDate = LocalDateTime.now();

                //Testing code - For showcase
                expirationDate = expirationDate.plusMinutes(5);
                Date expiration = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());

                //Deployment code - Business logic
//                expirationDate = expirationDate.plusMonths(6);
//                Date expiration = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());
                System.err.println("Company: " + company.getCompanyName() + " is deactivated!");

                //send email informing them of the deactivation of their account!!
                Boolean result = emailSessionBean.emailReminderAccountDeactivatedSync(company, company.getCompanyEmail());
                timerService.createSingleActionTimer(expiration, timerConfig);
            }
        } catch (CompanyDoesNotExistException | RefundCreationException | UnknownPersistenceException | RefundErrorException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * This method carries out automated checks on the each company's credit
     * balance to ensure that there is no negative amount
     */
//    @Schedule(hour = "7", minute = "0", second = "0", dayOfMonth = "20", month = "*", year = "*", persistent = false)
    @Schedule(hour = "*", minute = "*/2", second = "0", dayOfMonth = "*", month = "*", year = "*", persistent = false)
    public void automatedCheckCreditBalance() {
        System.out.println("Timer service triggered!");
        List<CompanyEntity> listOfCompanies = em.createQuery("SELECT coy FROM CompanyEntity coy WHERE coy.isDeleted = false").getResultList();
        for (CompanyEntity company : listOfCompanies) {
            System.err.println("Company Name: " + company.getCompanyName());
            if (company.getCreditOwned().intValueExact() <= 1000 && !company.getIsWarned()) {
                System.err.println("Timer Entry into condition <= 1000");

                Boolean result = emailSessionBean.emailCreditTopupNotificationSync(company, company.getCompanyEmail());
                company.setIsWarned(Boolean.TRUE);

                TimerConfig timerConfig = new TimerConfig(company, true);
                LocalDateTime expirationDate = LocalDateTime.now();

                //Testing code - For showcase
                expirationDate = expirationDate.plusMinutes(2);
                Date expiration = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());

                //Deployment Code - business logic
//                expirationDate = expirationDate.plusWeeks(1);
//                Date expiration = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());
                System.err.println("Expiry Period: " + expiration.toString());
                timerService.createSingleActionTimer(expiration, timerConfig);
                System.err.println("Email sent! Company: " + company.getCompanyName());

                if (!result) {
                    company.setWarningMessage("We have failed to contact you via email. Please top up your credit by end of the month! ");
                }
            }
        }
    }

    public List<PaymentEntity> retrieveAllHistoricalTransactions() {
        Query query = em.createQuery("SELECT p FROM PaymentEntity p");
        List<PaymentEntity> results = query.getResultList();

        for (PaymentEntity p : results) {
            if (p instanceof MonthlyPaymentEntity) {
                ((MonthlyPaymentEntity) p).getListOfProductLineItems().size();
            }
        }

        return results;
    }

    public List<PaymentEntity> retrieveSpecificHistoricalTransactions(Calendar startDate, Calendar endDate) {
        Query query = em.createQuery("SELECT p FROM PaymentEntity p WHERE p.dateTransacted >= :startDate AND p.dateTransacted <= :endDate");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<PaymentEntity> results = query.getResultList();

        for (PaymentEntity p : results) {
            if (p instanceof MonthlyPaymentEntity) {
                ((MonthlyPaymentEntity) p).getListOfProductLineItems().size();
            }
        }
        return results;
    }

    public MonthlyPaymentEntity retrieveCurrentMonthlyPaymentEntity(Calendar month) throws MonthlyPaymentNotFoundException {
        Calendar start = (Calendar) month.clone();
        Calendar end = (Calendar) month.clone();
        start.add(Calendar.DATE, 30);
        end.add(Calendar.DATE, -30);
        Query query = em.createQuery("SELECT mp FROM MonthlyPaymentEntity mp WHERE mp.dateGenerated >= :start AND mp.dateGenerated <= :end");
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<MonthlyPaymentEntity> results = query.getResultList();

        for (MonthlyPaymentEntity mp : results) {
            if (mp.getDateGenerated().get(Calendar.MONTH) == month.get(Calendar.MONTH)) {
                return mp;
            }
        }

        throw new MonthlyPaymentNotFoundException("Monthly Payment Invoice not found");
    }

    public List<MonthlyPaymentEntity> retrieveCurrentYearMonthlyPaymentEntity(Calendar year) throws MonthlyPaymentNotFoundException {
        int yearInt = year.get(Calendar.YEAR);
        Calendar start = new GregorianCalendar();
        start.set(yearInt, 1, 1);
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.MONTH, 12);

        Query query = em.createQuery("SELECT mp FROM MonthlyPaymentEntity mp WHERE mp.dateGenerated >= :start AND mp.dateGenerated <= :end");
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<MonthlyPaymentEntity> results = query.getResultList();

        if (results.isEmpty()) {
            throw new MonthlyPaymentNotFoundException("No Monthly Payment Invoice(s) are found");
        }

        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getDateGenerated().get(Calendar.YEAR) != yearInt) {
                results.remove(i);
            }
        }

        return results;
    }

    @Override
    public void resetPassword(String email) throws CompanyDoesNotExistException {
        try {
            CompanyEntity company = (CompanyEntity) em.createNamedQuery("findCustWithEmail").setParameter("custEmail", email).getSingleResult();

            int min = 0;
            int max = 999999999;

            int value = (int) (Math.random() * (max - min + 1) + min);
            String pathParam = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(String.valueOf(value)));

            company.setResetPasswordPathParam(pathParam);
            Calendar expiryDate = new GregorianCalendar();
            Calendar requestedDate = (Calendar) expiryDate.clone();
            expiryDate.add(GregorianCalendar.MINUTE, 30);
            company.setExpiryDateOfPathParam(expiryDate);

            // send email 
            emailSessionBean.emailResetPassword(company, pathParam, email, requestedDate);
        } catch (NoResultException ex) {
            throw new CompanyDoesNotExistException("Customer does not exists!");
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
