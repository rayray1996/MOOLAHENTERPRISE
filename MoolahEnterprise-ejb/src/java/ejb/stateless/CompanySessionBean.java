/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import com.sun.org.apache.bcel.internal.generic.DADD;
import com.sun.webkit.Timer;
import ejb.entity.CompanyEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.ProductEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
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

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @Resource 
    private SessionContext sessionContext;

    public CompanySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CompanyEntity createAccountForCompany(CompanyEntity newCompany) throws CompanyAlreadyExistException, UnknownPersistenceException, CompanyCreationException {
        Set<ConstraintViolation<CompanyEntity>> custError = validator.validate(newCompany);
        if (custError.isEmpty()) {
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
            throw new CompanyCreationException(prepareInputDataValidationErrorsMessage(custError));
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

    public void deactivateAccount(String email) throws CompanyDoesNotExistException {
        TimerService timerService = sessionContext.getTimerService();
        
        CompanyEntity company = retrieveCompanyByEmail(email);
        company.setIsDeactivated(true);
        TimerConfig timerConfig = new TimerConfig(company, true);
        LocalDateTime expirationDate = LocalDateTime.now();
        expirationDate.plusMonths(6);
        Date expiration = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());

        Timer deactivatedTimer = (Timer) timerService.createSingleActionTimer(expiration, timerConfig);
    }
    
    @Timeout
    public void timeoutCleanUp(Timer timer){
//        CompanyEntity company = timer.getInfo();
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
