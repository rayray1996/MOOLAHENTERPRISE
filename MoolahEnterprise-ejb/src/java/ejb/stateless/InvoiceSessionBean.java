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
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CompanyDoesNotExistException;
import util.exception.MonthlyPaymentNotFoundException;

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

    public Long purchaseMoolahCredits(Long companyId, BigInteger creditToBuy) throws CompanyDoesNotExistException {
        CompanyEntity company = em.find(CompanyEntity.class, companyId);

        // create a transaction
        CreditPaymentEntity payment = new CreditPaymentEntity(creditToBuy, Boolean.TRUE, Calendar.getInstance(), generatePaymentNumber(), moolahCreditConverter.convertCreditToSgd(creditToBuy), Calendar.getInstance(), company);
        em.persist(payment);
        em.flush();
        company.getListOfPayments().add(payment);

        // add credit to company
        companySessionBean.topupCredit(company, creditToBuy);

        return payment.getPaymentId();

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

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CompanyEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
