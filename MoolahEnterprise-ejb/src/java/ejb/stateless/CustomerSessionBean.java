/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.ComparisonEntity;
import ejb.entity.CustomerEntity;
import ejb.entity.ProductEntity;
import java.util.Set;
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
import util.exception.IncorrectLoginParticularsException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;

/**
 *
 * @author nickg
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createCustomer(CustomerEntity newCust) throws CustomerAlreadyExistException, UnknownPersistenceException, CustomerCreationException {
        Set<ConstraintViolation<CustomerEntity>> custError = validator.validate(newCust);
        if (custError.isEmpty()) {
            try {
                em.persist(newCust);
                em.flush();
                
                return newCust.getCustomerId(); 
            } catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CustomerAlreadyExistException(ex.getMessage());
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new CustomerCreationException(prepareInputDataValidationErrorsMessage(custError));
        }
    }

    //will retrieve all the way till product only
    public CustomerEntity login(String email, String password) throws IncorrectLoginParticularsException {
        try {
            CustomerEntity cust = (CustomerEntity) em.createNamedQuery("findCustWithEmail").setParameter("custEmail", email).getSingleResult();
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + cust.getSalt()));
            if(passwordHash.equals(cust.getPassword())){
                cust.getListOfIssues().size();
                cust.getListOfLikeProducts().size();
                cust.getListOfQuestionnaires().size();
                cust.getSavedComparisons().size();
                for(ComparisonEntity compareEntity : cust.getSavedComparisons()){
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
    
    public void resetPassword(String email, String password) throws CustomerPasswordExistsException, CustomerDoesNotExistsException{
        try{
            CustomerEntity cust = (CustomerEntity) em.createNamedQuery("findCustWithEmail").setParameter("custEmail", email).getSingleResult();
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + cust.getSalt()));
            
            if(passwordHash.equals(cust.getPassword())){
                throw new CustomerPasswordExistsException("Password cannot be the same!");
            } else {
                cust.setPassword(password);
            }
            
        } catch(NoResultException ex){
            throw new CustomerDoesNotExistsException("Customer does not exists!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomerEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
