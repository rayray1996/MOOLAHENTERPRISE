/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.Singleton.MoolahCreditConverterLocal;
import ejb.entity.CompanyEntity;
import ejb.entity.CreditPaymentEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.PointOfContactEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CompanyDoesNotExistException;
import util.exception.InvalidPaymentEntityCreationException;
import util.exception.InvalidPointOfContactCreationException;
import util.exception.MonthlyPaymentNotFoundException;
import util.exception.PaymentEntityAlreadyExistsException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rayta
 */
@Stateless
public class InvoiceSessionBean implements InvoiceSessionBeanLocal {

    @EJB
    private MoolahCreditConverterLocal moolahCreditConverter;

    @EJB
    private CompanySessionBeanLocal companySessionBean;

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public InvoiceSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public PaymentEntity retrieveMonthlyPaymentById(Long monthlyPaymentId) throws MonthlyPaymentNotFoundException {
        PaymentEntity monthlyPayment = em.find(PaymentEntity.class, monthlyPaymentId);
        if (monthlyPayment == null) {
            throw new MonthlyPaymentNotFoundException("Monthly payment is not found");
        } else {
            return monthlyPayment;
        }
    }

    public Long purchaseMoolahCredits(Long companyId, BigInteger creditToBuy) throws CompanyDoesNotExistException, InvalidPaymentEntityCreationException, InvalidPaymentEntityCreationException, UnknownPersistenceException, PaymentEntityAlreadyExistsException {
        CompanyEntity company = em.find(CompanyEntity.class, companyId);

        if(company == null) {
            throw new CompanyDoesNotExistException("Company is not found");
        }
        
        // create a transaction
        CreditPaymentEntity payment = new CreditPaymentEntity(creditToBuy, Boolean.TRUE, Calendar.getInstance(), generatePaymentNumber(), moolahCreditConverter.convertCreditToSgd(creditToBuy), Calendar.getInstance(), company);
        Set<ConstraintViolation<CreditPaymentEntity>> paymentError = validator.validate(payment);
        if (paymentError.isEmpty()) {
            try {
                em.persist(payment);
                em.flush();
                company.getListOfPayments().add(payment);

                // add credit to company
                companySessionBean.topupCredit(company, creditToBuy);

                return payment.getPaymentId();
                
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new PaymentEntityAlreadyExistsException("Credit payment already exists!");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InvalidPaymentEntityCreationException(prepareInputDataValidationErrorsMessage(paymentError));
        }

    }

    public void makePayment(Long paymentId, Long companyId) throws MonthlyPaymentNotFoundException, CompanyDoesNotExistException {
        CompanyEntity company = em.find(CompanyEntity.class, companyId);
        MonthlyPaymentEntity payment = em.find(MonthlyPaymentEntity.class, paymentId);

        if (company == null) {
            throw new CompanyDoesNotExistException("Company is not found");
        } else if (payment == null) {
            throw new MonthlyPaymentNotFoundException("Invoice is not found");
        } else {
            BigInteger amountPayable = payment.getTotalPayable();
            company.setCreditOwned(company.getCreditOwned().subtract(amountPayable));
            payment.setPaid(Boolean.TRUE);
        }
    }

    private String generatePaymentNumber() {
        Query query = em.createQuery("SELECT MAX(p.paymentId) FROM PaymentEntity p");
        try {
            Long paymentId = (Long) query.getSingleResult();
            paymentId++;
            String paymentNumber = "" + paymentId;
            while (paymentNumber.length() < 8) {
                paymentNumber = "0" + paymentNumber;
            }
            return "A" + paymentNumber;
        } catch (NoResultException ex) {
            return "A00000001";
        }
    }

    private <T> String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<T>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
