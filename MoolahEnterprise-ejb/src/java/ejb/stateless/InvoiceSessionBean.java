/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.CompanyEntity;
import ejb.entity.MonthlyPaymentEntity;
import java.math.BigInteger;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    private CompanySessionBeanLocal companySessionBean;

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public InvoiceSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public MonthlyPaymentEntity retrieveMonthlyPaymentById(Long monthlyPaymentId) throws MonthlyPaymentNotFoundException {
        MonthlyPaymentEntity monthlyPayment = em.find(MonthlyPaymentEntity.class, monthlyPaymentId);
        if (monthlyPayment == null) {
            throw new MonthlyPaymentNotFoundException("Monthly payment is not found");
        } else {
            return monthlyPayment;
        }
    }

    public Long purchaseMoolahCredits(Long companyId, BigInteger creditToBuy) throws CompanyDoesNotExistException {
        CompanyEntity company = em.find(CompanyEntity.class, companyId);

        companySessionBean.topupCredit(company, creditToBuy);

        // create a transaction
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CompanyEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
