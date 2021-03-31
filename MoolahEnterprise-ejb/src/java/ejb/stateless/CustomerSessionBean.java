/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.AssetEntity;
import ejb.entity.CompanyEntity;
import ejb.entity.ComparisonEntity;
import ejb.entity.CustomerEntity;
import ejb.entity.PremiumEntity;
import ejb.entity.ProductEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
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
import util.exception.AssetEntityDoesNotExistException;
import util.exception.CustomerAlreadyExistException;
import util.exception.CustomerCreationException;
import util.exception.CustomerDoesNotExistsException;
import util.exception.CustomerPasswordExistsException;
import util.exception.CustomerUpdateException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;

/**
 *
 * @author nickg
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @EJB
    private EmailSessionBeanLocal emailSessionBean;

    @EJB
    private ProductSessionBeanLocal productSessionBean;

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @Resource
    private SessionContext sessionContext;

    private TimerService timerService;

    public CustomerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @PostConstruct
    public void init() {
        timerService = sessionContext.getTimerService();

    }

    @Override
    public CustomerEntity createCustomer(CustomerEntity newCust) throws CustomerAlreadyExistException, UnknownPersistenceException, CustomerCreationException {
        System.out.println("CreateCustomer");
        Set<ConstraintViolation<CustomerEntity>> custError = validator.validate(newCust);
        if (custError.isEmpty()) {
            try {
                em.persist(newCust);
                em.flush();

                return newCust;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomerAlreadyExistException("Customer already exists!");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new CustomerCreationException(prepareInputDataValidationErrorsMessage(custError));
        }
    }

    //will retrieve all the way till ProductEntity only
    @Override
    public CustomerEntity login(String email, String password) throws IncorrectLoginParticularsException {
        try {
            CustomerEntity cust = (CustomerEntity) em.createNamedQuery("findCustWithEmail").setParameter("custEmail", email).getSingleResult();
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + cust.getSalt()));
            if (passwordHash.equals(cust.getPassword())) {
                cust.getListOfIssues().size();
                cust.getListOfLikeProducts().size();
                cust.getSavedComparisons().size();
                for (ComparisonEntity compareEntity : cust.getSavedComparisons()) {
                    compareEntity.getProductsToCompare().size();
                }

                return cust;
            } else {
                throw new IncorrectLoginParticularsException("Login particulars incorrect!");
            }

        } catch (NoResultException ex) {
            throw new IncorrectLoginParticularsException("Login particulars incorrect");
        }
    }

    @Override
    public void resetPassword(String email) throws CustomerPasswordExistsException, CustomerDoesNotExistsException {
        try {
            CustomerEntity cust = (CustomerEntity) em.createNamedQuery("findCustWithEmail").setParameter("custEmail", email).getSingleResult();

            int min = 0;
            int max = 999999999;

            int value = (int) (Math.random() * (max - min + 1) + min);
            String pathParam = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(String.valueOf(value)));

            cust.setResetPasswordPathParam(pathParam);
            Calendar expiryDate = new GregorianCalendar();
            Calendar requestedDate = (Calendar) expiryDate.clone();
            expiryDate.add(GregorianCalendar.MINUTE, 30);
            cust.setExpiryDateOfPathParam(expiryDate);

            // send email 
            emailSessionBean.emailResetPassword(cust, pathParam, email, requestedDate);

            TimerConfig timerConfig = new TimerConfig(cust, true);

            timerService.createSingleActionTimer(expiryDate.getTime(), timerConfig);

        } catch (NoResultException ex) {
            throw new CustomerDoesNotExistsException("Customer does not exists!");
        }
    }

    @Timeout
    @Override
    public void timeoutCleanUp(Timer timer) {
        try {
            CustomerEntity customer = (CustomerEntity) timer.getInfo();
            customer = retrieveCustomerById(customer.getCustomerId());
            System.err.println("TImeout method triggered for Customer Reset Pw! Customer: " + customer.getEmail());
            
            //remove the param
        } catch (CustomerDoesNotExistsException ex) {
            System.out.println("ERROR: CUSTOMER DOES NOT EXIST EX:" + ex.getMessage());
        }
    }

    @Override
    public void updateCustomer(CustomerEntity newCust) throws CustomerDoesNotExistsException, UnknownPersistenceException, CustomerUpdateException {
        Set<ConstraintViolation<CustomerEntity>> custError = validator.validate(newCust);
        if (custError.isEmpty()) {

            try {
                em.merge(newCust);
                em.flush();

            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomerDoesNotExistsException(ex.getMessage());
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new CustomerUpdateException(prepareInputDataValidationErrorsMessage(custError));
        }

    }

    @Override
    public CustomerEntity retrieveCustomerById(Long id) throws CustomerDoesNotExistsException {

        try {
            CustomerEntity cust = (CustomerEntity) em.createQuery("SELECT c FROM CustomerEntity c WHERE c.customerId =:cid").setParameter("cid", id).getSingleResult();
            cust.getListOfIssues().size();
            cust.getListOfLikeProducts().size();

            for (ComparisonEntity comp : cust.getSavedComparisons()) {
                comp.getProductsToCompare().size();
            }

            return cust;
        } catch (NoResultException ex) {
            throw new CustomerDoesNotExistsException("Customer with ID " + id + " does not exists!");
        }
    }

    @Override
    public CustomerEntity retrieveCustomerByParaLink(String path) throws CustomerDoesNotExistsException {
        try {
            CustomerEntity cust = (CustomerEntity) em.createQuery("SELECT c FROM CustomerEntity c WHERE c.resetPasswordPathParam =:pathParam").setParameter("pathParam", path).getSingleResult();
            cust.getListOfIssues().size();
            cust.getListOfLikeProducts().size();

            for (ComparisonEntity comp : cust.getSavedComparisons()) {
                comp.getProductsToCompare().size();
            }

            return cust;
        } catch (NoResultException ex) {
            throw new CustomerDoesNotExistsException("Customer with path paramater " + path + " does not exists!");
        }
    }

    @Override
    public void likeAProduct(Long custID, Long likedProdId) throws CustomerDoesNotExistsException, ProductNotFoundException {
        CustomerEntity cust = retrieveCustomerById(custID);
        ProductEntity prod = productSessionBean.retrieveProductEntityById(likedProdId);
        cust.getListOfLikeProducts().add(prod);
    }

    @Override
    public void removeLikedProduct(Long custId, Long prodId) throws ProductNotFoundException, CustomerDoesNotExistsException {
        CustomerEntity cust = retrieveCustomerById(custId);
        ProductEntity prod = productSessionBean.retrieveProductEntityById(prodId);

        for (ProductEntity likedProd : cust.getListOfLikeProducts()) {
            if (likedProd.getProductId().equals(prod.getProductId())) {
                cust.getListOfLikeProducts().remove(prod);
                break;
            }
        }

    }

    @Override
    public AssetEntity retrieveMyAsset(Long custId) throws CustomerDoesNotExistsException {
        CustomerEntity cust = retrieveCustomerById(custId);
        return cust.getAsset();
    }

    @Override
    public List<ProductEntity> viewLikedProductList(Long custId) throws CustomerDoesNotExistsException {
        CustomerEntity cust = retrieveCustomerById(custId);
        cust.getListOfLikeProducts().size();
        return cust.getListOfLikeProducts();
    }

    @Override
    public List<ProductEntity> retrieveRecommendedProducts(Long customerId) throws CustomerDoesNotExistsException, ProductNotFoundException {
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);

        if (customer == null) {
            throw new CustomerDoesNotExistsException("Customer is not found");
        }

        // get isMarried and age of customer
        // Customer's basic information
        boolean isMarried = customer.getIsMarried();
        int age = getAgeOfCustomer(customer);
        List<BigDecimal> nextThreeYearsOfCapital = getThreeYearsOfCapital(customerId);
        BigDecimal yearOneCapital = nextThreeYearsOfCapital.get(0);
        BigDecimal yearTwoCapital = nextThreeYearsOfCapital.get(1);
        BigDecimal yearThreeCapital = nextThreeYearsOfCapital.get(2);

        /*
        Recommend products based on 3 years affordability
         */
//        Query query = em.createQuery("SELECT p FROM ProductEntity p WHERE p.isDeleted = false AND p.company.isDeleted = false AND p.company.isDeactivated = false");
        Query query = em.createQuery("SELECT p FROM ProductEntity p, IN (p.listOfPremium) premium WHERE p.isDeleted = false AND p.company.isDeleted = false AND p.company.isDeactivated = false AND :age >= premium.minAgeGroup AND :age <= premium.maxAgeGroup AND :yearOne >= premium.value AND :yearTwo >= premium.value AND :yearThree >= premium.value");
        query.setParameter("age", age);
        query.setParameter("yearOne", yearOneCapital);
        query.setParameter("yearTwo", yearOneCapital);
        query.setParameter("yearThree", yearOneCapital);

        List<ProductEntity> allProducts = query.getResultList();

//        for (int i = 0; i < allProducts.size(); i++) {
//            ProductEntity currProductEntity = allProducts.get(i);
//            if (!canAffordProduct(customer, currProductEntity, nextThreeYearsOfCapital)) {
//                allProducts.remove(i);
//            }
//        }
        for (ProductEntity p : allProducts) {
            p.getListOfAdditionalFeatures().size();
            p.getListOfPremium().size();
            p.getListOfRiders().size();
        }

        return allProducts;

    }

    @Override
    public boolean canAffordProduct(CustomerEntity customer, ProductEntity product,
            List<BigDecimal> nextThreeYearsOfCapital) throws ProductNotFoundException, CustomerDoesNotExistsException {
        if (product == null) {
            throw new ProductNotFoundException("No product is selected");
        }

        if (customer == null) {
            throw new CustomerDoesNotExistsException("No customer is selected");
        }

        // get the age premium
        PremiumEntity currPremium = null;
        Integer age = getAgeOfCustomer(customer);

        for (PremiumEntity premium : product.getListOfPremium()) {
            if (age >= premium.getMinAgeGroup() && age <= premium.getMaxAgeGroup()) {
                currPremium = premium;
                break;
            }
        }

        if (currPremium == null) {
            throw new ProductNotFoundException("Selected producted does not contain the premium for customer's age");
        }

        for (BigDecimal capital : nextThreeYearsOfCapital) {
            if (capital.compareTo(currPremium.getPremiumValue()) < 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<BigDecimal> getThreeYearsOfCapital(Long customerId) throws CustomerDoesNotExistsException {
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);

        if (customer == null) {
            throw new CustomerDoesNotExistsException("Customer is not found");
        }

        BigDecimal yearlyIncrement = (customer.getAsset().getMonthlyIncome().subtract(customer.getAsset().getMonthlyExpense())).multiply(BigDecimal.valueOf(12));
        BigDecimal yearOneCapital = customer.getAsset().getCashInHand();
        BigDecimal yearTwoCapital = yearOneCapital.add(yearlyIncrement);
        BigDecimal yearThreeCapital = yearTwoCapital.add(yearlyIncrement);

        List<BigDecimal> results = new ArrayList<>();
        results.add(yearOneCapital);
        results.add(yearTwoCapital);
        results.add(yearThreeCapital);

        return results;
    }

    @Override
    public Integer getAgeOfCustomer(CustomerEntity customer
    ) {
        int currentYear = new GregorianCalendar().get(GregorianCalendar.YEAR);
        int birthYear = customer.getDateOfBirth().get(GregorianCalendar.YEAR);

        return currentYear - birthYear;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomerEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

  

}
