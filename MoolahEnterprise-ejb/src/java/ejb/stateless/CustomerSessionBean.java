/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.AssetEntity;
import ejb.entity.ComparisonEntity;
import ejb.entity.CustomerEntity;
import ejb.entity.ProductEntity;
import ejb.entity.QuestionEntity;
import ejb.entity.QuestionnaireEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
    private ProductSessionBeanLocal productSessionBean;

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CustomerEntity createCustomer(CustomerEntity newCust) throws CustomerAlreadyExistException, UnknownPersistenceException, CustomerCreationException {
        Set<ConstraintViolation<CustomerEntity>> custError = validator.validate(newCust);
        if (custError.isEmpty()) {
            try {
                em.persist(newCust);
                em.flush();

                return newCust;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomerAlreadyExistException(ex.getMessage());
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

    //will retrieve all the way till product only
    @Override
    public CustomerEntity login(String email, String password) throws IncorrectLoginParticularsException {
        try {
            CustomerEntity cust = (CustomerEntity) em.createNamedQuery("findCustWithEmail").setParameter("custEmail", email).getSingleResult();
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + cust.getSalt()));
            if (passwordHash.equals(cust.getPassword())) {
                cust.getListOfIssues().size();
                cust.getListOfLikeProducts().size();
                cust.getListOfQuestionnaires().size();
                cust.getSavedComparisons().size();
                for (ComparisonEntity compareEntity : cust.getSavedComparisons()) {
                    compareEntity.getProductsToCompare().size();
                }

                return cust;
            } else {
                throw new IncorrectLoginParticularsException("Login particulars incorrect!");
            }

        } catch (NoResultException ex) {
            throw new IncorrectLoginParticularsException(ex.getMessage());
        }
    }

    @Override
    public void resetPassword(String email, String password) throws CustomerPasswordExistsException, CustomerDoesNotExistsException {
        try {
            CustomerEntity cust = (CustomerEntity) em.createNamedQuery("findCustWithEmail").setParameter("custEmail", email).getSingleResult();
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + cust.getSalt()));

            if (passwordHash.equals(cust.getPassword())) {
                throw new CustomerPasswordExistsException("Password cannot be the same!");
            } else {
                cust.setPassword(password);
            }

        } catch (NoResultException ex) {
            throw new CustomerDoesNotExistsException("Customer does not exists!");
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
            cust.getListOfQuestionnaires();
            for (QuestionnaireEntity qn : cust.getListOfQuestionnaires()) {
                for (QuestionEntity questionEntity : qn.getListOfQuestions()) {
                    questionEntity.getListOfOptions().size();
                }
            }

            for (ComparisonEntity comp : cust.getSavedComparisons()) {
                comp.getProductsToCompare().size();
            }

            return cust;
        } catch (NoResultException ex) {
            throw new CustomerDoesNotExistsException("Customer with ID " + id + " does not exists!");
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

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomerEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
