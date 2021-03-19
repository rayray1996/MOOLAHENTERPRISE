/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.FeatureEntity;
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
import util.exception.FeatureAlreadyExistsException;
import util.exception.FeatureCreationException;
import util.exception.FeatureDoesNotExistsException;
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Stateless
public class FeatureSessionBean implements FeatureSessionBeanLocal {

    @EJB
    private ProductSessionBeanLocal productSessionBean;

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FeatureSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public List<FeatureEntity> retrieveListOfFeatures(Long productId) throws ProductNotFoundException {
        ProductEntity product = productSessionBean.retrieveProductEntityById(productId);
        List<FeatureEntity> listOfFeatures = product.getListOfAdditionalFeatures();
        return listOfFeatures;
    }

    @Override
    public FeatureEntity retrieveFeatureEntitybyId(Long featureId) throws FeatureDoesNotExistsException {
        FeatureEntity feature = em.find(FeatureEntity.class, featureId);
        if (feature == null) {
            throw new FeatureDoesNotExistsException("Feature does not exists!");
        } else {
            return feature;
        }
    }

    @Override
    public void deleteFeatureEntity(Long featureId) throws FeatureDoesNotExistsException, ProductNotFoundException {
        FeatureEntity feature = em.find(FeatureEntity.class, featureId);
        if (feature == null) {
            throw new FeatureDoesNotExistsException("Feature does not exists!");
        } else {
            try {
                ProductEntity product = (ProductEntity) em.createQuery("SELECT p FROM ProductEntity p, IN (p.listOfAdditionalFeatures) f WHERE f.featureId =:feature").setParameter("feature", featureId).getSingleResult();
                product.getListOfAdditionalFeatures().remove(feature);
            } catch (NoResultException ex) {
                throw new ProductNotFoundException("Product containing this feature cannot be found!");
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FeatureEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
