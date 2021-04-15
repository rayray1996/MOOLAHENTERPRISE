/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.PremiumEntity;
import ejb.entity.ProductEntity;
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
import util.exception.PremiumAlreadyExistException;
import util.exception.PremiumCreationException;
import util.exception.PremiumDoesNotExistException;
import util.exception.ProductIsDeletedException;
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Stateless
public class PremiumSessionBean implements PremiumSessionBeanLocal {

    @EJB
    private ProductSessionBeanLocal productSessionBean;

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public PremiumSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    //do retrieve method and 1 more for feature entity
    @Override
    public List<PremiumEntity> retrieveListOfPremiumEntityForProduct(Long productId) throws ProductNotFoundException, ProductIsDeletedException {
        ProductEntity product = productSessionBean.retrieveProductEntityById(productId);
        List<PremiumEntity> listOfPremium = product.getListOfPremium();
        return listOfPremium;
    }

    @Override
    public PremiumEntity retrievePremiumEntityById(Long premiumId) throws PremiumDoesNotExistException {
        PremiumEntity premium = em.find(PremiumEntity.class, premiumId);
        if (premium == null) {
            throw new PremiumDoesNotExistException("Premium with ID " + premiumId + " does not exists!");
        } else {
            return premium;
        }
    }

    //do retrieve method and 1 more for feature entity
    @Override
    public List<PremiumEntity> retrieveListOfSmokerPremiumEntityForProduct(Long productId) throws ProductNotFoundException, ProductIsDeletedException {
        ProductEntity product = productSessionBean.retrieveProductEntityById(productId);
        List<PremiumEntity> listOfPremium = product.getListOfSmokerPremium();
        return listOfPremium;
    }
    
    @Override
    public void deletePremium(Long premiumId) throws PremiumDoesNotExistException, ProductNotFoundException {
        PremiumEntity premiumToDelete = em.find(PremiumEntity.class, premiumId);
        if (premiumToDelete == null) {
            throw new PremiumDoesNotExistException("Premium with ID " + premiumId + " does not exists!");
        } else {
            try {
                ProductEntity product = (ProductEntity) em.createQuery("SELECT p FROM ProductEntity p, IN (p.listOfPremium) r WHERE r.premiumId =:premium ").setParameter("premium", premiumId).getSingleResult();
                product.getListOfPremium().remove(premiumToDelete);
            } catch (NoResultException ex) {
                throw new ProductNotFoundException("ERR Product for premium cannot be found!");
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PremiumEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
