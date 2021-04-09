/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.ProductEntity;
import ejb.entity.RiderEntity;
import java.util.List;
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
import util.exception.ProductNotFoundException;
import util.exception.RiderAlreadyExistException;
import util.exception.RiderCreationException;
import util.exception.RiderDoesNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Stateless
public class RiderSessionBean implements RiderSessionBeanLocal {
    
    @EJB
    private ProductSessionBeanLocal productSessionBean;
    
    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public RiderSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public RiderEntity retrieveRiderByRiderID(Long riderId) throws RiderDoesNotExistException {
        RiderEntity rider = em.find(RiderEntity.class, riderId);
        if (rider == null) {
            throw new RiderDoesNotExistException("Rider with ID: " + riderId + " does not exists!");
        } else {
            return rider;
        }
    }
    
    @Override
    public List<RiderEntity> retrieveListOfRiderEntityForProduct(Long productId) throws ProductNotFoundException {
        ProductEntity product = em.find(ProductEntity.class, productId);
        if (product == null) {
            throw new ProductNotFoundException("Product with ID " + productId + " cannot be found!");
        } else {
            List<RiderEntity> listofRider = product.getListOfRiders();
            return listofRider;
        }
    }
    
    @Override
    public void deleteRider(Long riderId) throws RiderDoesNotExistException, ProductNotFoundException {
        RiderEntity rider = em.find(RiderEntity.class, riderId);
        if (rider == null) {
            throw new RiderDoesNotExistException("Rider with ID: " + riderId + " does not exists!");
        } else {
            try {
                ProductEntity prod = (ProductEntity) em.createQuery("SELECT p FROM ProductEntity p, IN (p.listOfRiders) r WHERE r.riderId =:rider ").setParameter("rider", riderId).getSingleResult();
                prod.getListOfRiders().remove(rider);
            } catch (NoResultException ex) {
                throw new ProductNotFoundException("Product cannot be found!");
            }
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RiderEntity>> constraintViolations) {
        String msg = "Input data validation error!:";
        
        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    public List<RiderEntity> listOfEntity() {
        List<RiderEntity> riderList;
        Query query = em.createQuery("SELECT s FROM RiderEntity s");
        riderList = query.getResultList();
        for (RiderEntity rl : riderList) {
            em.detach(rl);
        }
        return riderList;
    }
}
