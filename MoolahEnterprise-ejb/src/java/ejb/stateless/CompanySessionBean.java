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
import ejb.entity.PointOfContactEntity;
import ejb.entity.ProductEntity;
import ejb.entity.RefundEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
import util.exception.CompanyBeanValidaionException;
import util.exception.CompanyCreationException;
import util.exception.CompanyDoesNotExistException;
import util.exception.CompanySQLConstraintException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.MonthlyPaymentNotFoundException;
import util.exception.PointOfContactBeanValidationException;
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
    public CompanyEntity createAccountForCompanyWS(CompanyEntity newCompany) throws CompanyAlreadyExistException, UnknownPersistenceException, CompanyCreationException, PointOfContactBeanValidationException {
        Set<ConstraintViolation<CompanyEntity>> companyError = validator.validate(newCompany);
        if (companyError.isEmpty()) {
            try {
                if (newCompany.getListOfPointOfContacts() != null && !newCompany.getListOfPointOfContacts().isEmpty()) {
                    for (PointOfContactEntity pe : newCompany.getListOfPointOfContacts()) {
                        Set<ConstraintViolation<PointOfContactEntity>> pointOfContactError = validator.validate(pe);
                        if (!pointOfContactError.isEmpty()) {
                            throw new PointOfContactBeanValidationException("Please check your point of contact input");
                        }

                    }
                }
                List<PointOfContactEntity> pocs = new ArrayList<>(newCompany.getListOfPointOfContacts());
                for (PointOfContactEntity poc : pocs) {
                    poc.setCompany(newCompany);
                }
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
            if (coy.getListOfProducts() != null && !coy.getListOfProducts().isEmpty()) {
                coy.getListOfProducts().size();
            }
            if (coy.getListOfPayments() != null && !coy.getListOfPayments().isEmpty()) {
                coy.getListOfPayments().size();
            }
            if (coy.getListOfPointOfContacts() != null && !coy.getListOfPointOfContacts().isEmpty()) {
                coy.getListOfPointOfContacts().size();
            }
            coy.getRefund();
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
    public CompanyEntity updateCompanyInformationWS(CompanyEntity company) throws UnknownPersistenceException, CompanySQLConstraintException, PointOfContactBeanValidationException, CompanyBeanValidaionException, IncorrectLoginParticularsException {
        Set<ConstraintViolation<CompanyEntity>> companyError = validator.validate(company);
        if (companyError.isEmpty()) {

            try {
//                company = em.find(CompanyEntity.class, company.getCompanyId());
                String oldPassword = new String(company.getPassword());
                String oldSalt = new String(company.getSalt());
//                String attemptPassword = password;
//                System.out.println("attemptPassword = " + attemptPassword);
                System.out.println("oldPassword = " + oldPassword);
                System.out.println("OldSalt : " + oldSalt);
//                System.out.println("");
//                
                System.out.println("oldEmail = " + company.getCompanyEmail());
//                System.out.println("attemptEmail = " + email);
//                String oldEmail = company.getCompanyEmail();
//                String attemptEmail = email;
//                if (!(oldEmail.equals(attemptEmail) && oldPassword.equals(attemptPassword))) {
//                    throw new IncorrectLoginParticularsException("Incorrect login details provided!");
//                }
                if (company.getListOfPointOfContacts() != null && !company.getListOfPointOfContacts().isEmpty()) {
                    for (PointOfContactEntity pe : company.getListOfPointOfContacts()) {
                        Set<ConstraintViolation<PointOfContactEntity>> pointOfContactError = validator.validate(pe);
                        if (!pointOfContactError.isEmpty()) {
                            throw new PointOfContactBeanValidationException("Please check your point of contact input");
                        }

                    }

                    for (ProductEntity product : company.getListOfProducts()) {
                        product.setCompany(company);
                    }
                    if (company.getRefund() != null) {
                        company.getRefund().setCompany(company);
                    }
                    for (PaymentEntity payment : company.getListOfPayments()) {
                        payment.setCompany(company);
                    }
                    for (PointOfContactEntity pointOfContact : company.getListOfPointOfContacts()) {
                        pointOfContact.setCompany(company);
                    }
                    em.merge(company);

                    em.flush();
                    company.setSalt(oldSalt);
                    company.setPassword(oldPassword);
                    System.out.println("New Pw: " + company.getPassword());
                    System.out.println("New Salt: " + company.getSalt());
                    return company;
                }
                throw new CompanyBeanValidaionException("Please check your point of contacts");

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
//            catch (IncorrectLoginParticularsException ex) {
//                throw new IncorrectLoginParticularsException("Incorrect login details provided!");
//            }
        } else {
            throw new CompanyBeanValidaionException(prepareInputDataValidationErrorsMessage(companyError));
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
//    @Schedule(hour = "*/5", minute = "0", second = "0", dayOfMonth = "*", month = "*", year = "*", persistent = false)
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

    @Override
    public List<MonthlyPaymentEntity> retrieveAllUnpaidPayment() {
        Query query = em.createQuery("SELECT mp FROM MonthlyPaymentEntity mp WHERE mp.paid = false");
        return query.getResultList();
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

    @Override
    public List<PaymentEntity> retrieveSpecificHistoricalTransactions(Calendar startDate, Calendar endDate, Long coyId) {
        System.out.println("******************startDate:" + startDate.getTime() + " endDate:" + endDate.getTime() + " coyId" + coyId);
        Query query = em.createQuery("SELECT p FROM PaymentEntity p WHERE p.dateTransacted >= :startDate AND p.dateTransacted <= :endDate AND  p.company.companyId =:coyId");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("coyId", coyId);
        List<PaymentEntity> results = query.getResultList();
        System.out.println("********p*************");
        for (PaymentEntity p : results) {
            System.out.println("********p:" + p.getPaymentId());
            if (p instanceof MonthlyPaymentEntity) {
                ((MonthlyPaymentEntity) p).getListOfProductLineItems().size();
            }
        }
        return results;
    }

    /**
     * Error: exception Description: Problem compiling [SELECT mp FROM
     * MonthlyPaymentEntity mp WHERE mp.dateGenerated >= :start AND
     * mp.dateGenerated <= :end AND mp.companyId =:coyId]. [105, 117] The state
     * field path 'mp.companyId' cannot be resolved to a valid type. @param
     * month @param coyId @return @throws MonthlyPaymentNotFoundException
     */
    @Override
    public MonthlyPaymentEntity retrieveCurrentMonthlyPaymentEntity(Calendar month, Long coyId) throws MonthlyPaymentNotFoundException {
        Calendar start = (Calendar) month.clone();
        Calendar end = (Calendar) month.clone();
        start.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH), 1, 0, 0);
        end.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH) + 1, 1, 0, 0);

//        start.add(Calendar.DATE, 30);
//        end.add(Calendar.DATE, -30);
        Query query = em.createQuery("SELECT mp FROM PaymentEntity mp WHERE mp.dateGenerated >= :start AND mp.dateGenerated <= :end AND mp.company.companyId =:coyId");
        query.setParameter("start", start);
        query.setParameter("end", end);
        query.setParameter("coyId", coyId);

        List<PaymentEntity> results = query.getResultList();
        MonthlyPaymentEntity mthlyPmt = null;

        for (PaymentEntity pe : results) {
            if (pe instanceof MonthlyPaymentEntity) {
                mthlyPmt = (MonthlyPaymentEntity) pe;

                return mthlyPmt;

            }
        }

        throw new MonthlyPaymentNotFoundException("Monthly Payment Invoice not found");
//        if(results!=null ){
//            return results;
//        }
//        for (MonthlyPaymentEntity mp : results) {
//            if (mp.getDateGenerated().get(Calendar.MONTH) == month.get(Calendar.MONTH)) {
//                return mp;
//            }
//        }
//        throw new MonthlyPaymentNotFoundException("Monthly Payment Invoice not found");
    }

    @Override
    public List<MonthlyPaymentEntity> retrieveCurrentYearMonthlyPaymentEntity(Calendar year, Long coyId) throws MonthlyPaymentNotFoundException {
        int yearInt = year.get(Calendar.YEAR);
        Calendar start = new GregorianCalendar();
        start.set(yearInt, 0, 1);
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.MONTH, 12);

        Query query = em.createQuery("SELECT mp FROM PaymentEntity mp WHERE mp.dateGenerated >= :start AND mp.dateGenerated <= :end AND mp.company.companyId=:coyId");
        query.setParameter("start", start);
        query.setParameter("end", end);
        query.setParameter("coyId", coyId);
        List<PaymentEntity> results = query.getResultList();
        List<MonthlyPaymentEntity> finalResult = new ArrayList<>();

        for (PaymentEntity mp : results) {
            if (mp instanceof MonthlyPaymentEntity) {
                finalResult.add((MonthlyPaymentEntity) mp);
            }
        }

        if (finalResult.isEmpty()) {
            throw new MonthlyPaymentNotFoundException("No Monthly Payment Invoice(s) are found");
        }

        for (int i = 0; i < finalResult.size(); i++) {
            if (finalResult.get(i).getDateGenerated().get(Calendar.YEAR) != yearInt) {
                finalResult.remove(i);
            }
        }

        return finalResult;
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
