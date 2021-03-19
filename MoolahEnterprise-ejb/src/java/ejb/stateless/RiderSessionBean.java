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
    public RiderEntity createRider(RiderEntity newRider, Long productId) throws RiderAlreadyExistException, UnknownPersistenceException, RiderCreationException, ProductNotFoundException {
        Set<ConstraintViolation<RiderEntity>> riderError = validator.validate(newRider);
        if (riderError.isEmpty()) {
            try {
                em.persist(newRider);
                em.flush();
                
                ProductEntity product = productSessionBean.retrieveProductEntityById(productId);
                product.getListOfRiders().add(newRider);

                return newRider;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new RiderAlreadyExistException(ex.getMessage());
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new RiderCreationException(prepareInputDataValidationErrorsMessage(riderError));
        }
    }

    @Override
    public void updateRider(RiderEntity updateRider) throws RiderAlreadyExistException, UnknownPersistenceException {
        try {
            em.merge(updateRider);
            em.flush();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new RiderAlreadyExistException("Rider already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
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
    public List<RiderEntity> retrieveListOfRiderEntityForProduct(Long productId) throws ProductNotFoundException{
        ProductEntity product = em.find(ProductEntity.class, productId);
        if(product == null){
            throw new ProductNotFoundException("Product with ID "  + productId + " cannot be found!");
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
}