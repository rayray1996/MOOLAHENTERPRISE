/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.EndowmentEntity;
import ejb.entity.InvestmentLinkedEntity;
import ejb.entity.ProductEntity;
import ejb.entity.TermLifeProductEntity;
import ejb.entity.WholeLifeProductEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.ProductNotFoundException;

/**
 *
 * @author rayta
 */
@Stateless
public class ProductSessionBean implements ProductSessionBeanLocal {

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ProductSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public List<ProductEntity> retrieveAllFinancialProducts() {
        Query query = em.createQuery("SELECT p FROM ProductEntity p");
        List<ProductEntity> results = query.getResultList();

        for (ProductEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfAdditionalFeatures().size();
        }
        return results;
    }

    @Override
    public List<EndowmentEntity> retrieveAllEndowmentProducts() {
        Query query = em.createQuery("SELECT e FROM EndowmentEntity e");
        List<EndowmentEntity> results = query.getResultList();

        for (EndowmentEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfAdditionalFeatures().size();
        }

        return results;
    }

    @Override
    public List<InvestmentLinkedEntity> retrieveAllInvestmentLinkedProducts() {
        Query query = em.createQuery("SELECT i FROM InvestmentLinkedEntity i");
        List<InvestmentLinkedEntity> results = query.getResultList();

        for (InvestmentLinkedEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfAdditionalFeatures().size();
        }
        return results;
    }

    @Override
    public List<TermLifeProductEntity> retrieveAllTermLifeProducts() {
        Query query = em.createQuery("SELECT t FROM TermLifeProductEntity t");
        List<TermLifeProductEntity> results = query.getResultList();

        for (TermLifeProductEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfAdditionalFeatures().size();
        }
        return results;
    }

    @Override
    public List<WholeLifeProductEntity> retrieveAllWholeLifeProducts() {
        Query query = em.createQuery("SELECT w FROM WholeLifeProductEntity w");
        List<WholeLifeProductEntity> results = query.getResultList();

        for (WholeLifeProductEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfAdditionalFeatures().size();
        }
        return results;
    }

    @Override
    public ProductEntity retrieveProductEntityById(Long productId) throws ProductNotFoundException {
        ProductEntity product = em.find(ProductEntity.class, productId);
        if (product == null) {
            throw new ProductNotFoundException("Product is not found");
        } else {
            product.getListOfAdditionalFeatures().size();
            product.getListOfPremium().size();
            product.getListOfAdditionalFeatures().size();
            return product;
        }
    }

    @Override
    public List<ProductEntity> searchForProductsByName(String name) {
        Query query = em.createQuery("SELECT p FROM ProductEntity p WHERE p.productName LIKE :name");
        query.setParameter("name", "%" + name + "%");
        List<ProductEntity> results = query.getResultList();

        for (ProductEntity p : results) {
            p.getListOfAdditionalFeatures().size();
            p.getListOfPremium().size();
            p.getListOfAdditionalFeatures().size();
        }
        
        return results;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ProductEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}