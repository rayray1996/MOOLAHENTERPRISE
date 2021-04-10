/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import util.exception.ProductLineItemException;
import ejb.Singleton.MoolahCreditConverterLocal;
import ejb.entity.ClickThroughEntity;
import ejb.entity.CompanyEntity;
import ejb.entity.CreditPaymentEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.ProductEntity;
import ejb.entity.ProductLineItemEntity;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
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
import util.exception.MonthlyPaymentAlreadyExistsException;
import util.exception.MonthlyPaymentException;
import util.exception.MonthlyPaymentNotFoundException;
import util.exception.PaymentEntityAlreadyExistsException;
import util.exception.ProductLineItemAlreadyExistException;
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rayta
 */
@Stateless
public class InvoiceSessionBean implements InvoiceSessionBeanLocal {

    @EJB
    private EmailSessionBeanLocal emailSessionBean;

    @EJB
    private ClickthroughSessionBeanLocal clickthroughSessionBean;

    @EJB
    private ProductSessionBeanLocal productSessionBean;

    @EJB
    private MoolahCreditConverterLocal moolahCreditConverter;

    @EJB
    private CompanySessionBeanLocal companySessionBean;

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @Resource
    private SessionContext sessionContext;

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

        if (company == null) {
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
            Calendar date = Calendar.getInstance();
            payment.setDateTransacted(date);
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

    /**
     * This method generates the monthly invoice for companies in the system. It
     * sends an email out to remind them regarding the pending payment,
     * requesting them to log in and complete payment
     */
    //    @Schedule(hour = "7", minute = "0", second = "0", dayOfMonth = "1", month = "*", year = "*", persistent = false)
    @Schedule(hour = "0", minute = "*/4", second = "0", dayOfMonth = "*", month = "*", year = "*", persistent = false)
    @Override
    public void automatedMonthlyInvoice() {
        try {
            System.out.println("*****************************************Invoice triggered!");
            List<CompanyEntity> listOfCompanies = companySessionBean.retrieveAllActiveCompanies();
            for (CompanyEntity company : listOfCompanies) {
                List<ProductEntity> listOfProducts = productSessionBean.retrieveListOfProductByCompany(company.getCompanyEmail());

                Calendar date = Calendar.getInstance();
                //This monthly payment has yet to be paid by the company
                MonthlyPaymentEntity monthlyPayment = new MonthlyPaymentEntity(date, company);
                monthlyPayment = createMonthlyPayment(monthlyPayment);
                company.getListOfPayments().add(monthlyPayment);

                for (ProductEntity product : listOfProducts) {

                    ClickThroughEntity clickThrough = product.getClickThroughInfo();
                    BigInteger monthlyClicks = clickThrough.getMonthCounter();
                    BigInteger monthlySubtotal = clickthroughSessionBean.calculateMonthlyProductPrice(product.getProductId());
                    BigInteger monthlySubscription = product.getProductCategoryPricing().getFixedSubscriptionCredit();

                    ProductLineItemEntity newProdLineItem = new ProductLineItemEntity(product, monthlyClicks, monthlySubtotal, monthlySubscription);
                    newProdLineItem = createProductLineItem(newProdLineItem);
                    monthlyPayment.getListOfProductLineItems().add(newProdLineItem);

                    BigInteger newMonthlySubtotal = monthlyPayment.getTotalPayable().add(newProdLineItem.getMonthlySubtotalCredit());
                    monthlyPayment.setTotalPayable(newMonthlySubtotal);

                }
                System.out.println("Check prior to email sending");
                emailSessionBean.emailMonthlyPaymentInvoice(monthlyPayment, company.getCompanyEmail());
                System.out.println("Email sent for: " + company.getCompanyName());
            }
        } catch (CompanyDoesNotExistException | MonthlyPaymentAlreadyExistsException | UnknownPersistenceException | MonthlyPaymentException
                | ProductNotFoundException | ProductLineItemAlreadyExistException | ProductLineItemException ex) {
            System.out.println(ex.getMessage());
            sessionContext.setRollbackOnly();
        }

    }

    @Override
    public MonthlyPaymentEntity createMonthlyPayment(MonthlyPaymentEntity newMonthlyPayment) throws MonthlyPaymentAlreadyExistsException, UnknownPersistenceException, MonthlyPaymentException {
        Set<ConstraintViolation<MonthlyPaymentEntity>> monthlyPaymentError = validator.validate(newMonthlyPayment);
        if (monthlyPaymentError.isEmpty()) {
            try {
                em.persist(newMonthlyPayment);
                em.flush();

                return newMonthlyPayment;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new MonthlyPaymentAlreadyExistsException("Monthly Payment already exists!");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new MonthlyPaymentException(prepareInputDataValidationErrorsMessageMonthlyPayment(monthlyPaymentError));
        }
    }

    @Override
    public ProductLineItemEntity createProductLineItem(ProductLineItemEntity newProductLineItem) throws ProductLineItemAlreadyExistException, UnknownPersistenceException, ProductLineItemException {
        Set<ConstraintViolation<ProductLineItemEntity>> productLineItemError = validator.validate(newProductLineItem);
        if (productLineItemError.isEmpty()) {
            try {
                em.persist(newProductLineItem);
                em.flush();

                return newProductLineItem;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ProductLineItemAlreadyExistException("Product Line Item already exists!");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new ProductLineItemException(prepareInputDataValidationErrorsMessageProductLineItem(productLineItemError));
        }
    }

    private String prepareInputDataValidationErrorsMessageMonthlyPayment(Set<ConstraintViolation<MonthlyPaymentEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    private String prepareInputDataValidationErrorsMessageProductLineItem(Set<ConstraintViolation<ProductLineItemEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    private <T> String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<T>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
