/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.CompanyEntity;
import ejb.entity.EndowmentEntity;
import ejb.entity.ProductEntity;
import ejb.entity.TermLifeProductEntity;
import ejb.entity.WholeLifeProductEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CategoryEnum;
import util.exception.CompanyCreationException;
import util.exception.CompanyDoesNotExistException;
import util.exception.InvalidFilterCriteriaException;
import util.exception.InvalidProductCreationException;
import util.exception.ProductAlreadyExistsException;
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rayta
 */
@Stateless
public class ProductSessionBean implements ProductSessionBeanLocal {

    @EJB
    private CompanySessionBeanLocal companySessionBean;

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
        Query query = em.createQuery("SELECT p FROM ProductEntity p WHERE p.isDeleted = FALSE");
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
        Query query = em.createQuery("SELECT e FROM EndowmentEntity e WHERE e.isDeleted = FALSE");
        List<EndowmentEntity> results = query.getResultList();

        for (EndowmentEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfAdditionalFeatures().size();
        }

        return results;
    }

//    @Override
//    public List<InvestmentLinkedEntity> retrieveAllInvestmentLinkedProducts() {
//        Query query = em.createQuery("SELECT i FROM InvestmentLinkedEntity i WHERE i.isDeleted = FALSE");
//        List<InvestmentLinkedEntity> results = query.getResultList();
//
//        for (InvestmentLinkedEntity e : results) {
//            e.getListOfAdditionalFeatures().size();
//            e.getListOfPremium().size();
//            e.getListOfAdditionalFeatures().size();
//        }
//        return results;
//    }

    @Override
    public List<TermLifeProductEntity> retrieveAllTermLifeProducts() {
        Query query = em.createQuery("SELECT t FROM TermLifeProductEntity t WHERE t.isDeleted = FALSE");
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
        Query query = em.createQuery("SELECT w FROM WholeLifeProductEntity w WHERE w.isDeleted = FALSE");
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
        Query query = em.createQuery("SELECT p FROM ProductEntity p WHERE p.productName LIKE :name AND  WHERE p.isDeleted = FALSE");
        query.setParameter("name", "%" + name + "%");
        List<ProductEntity> results = query.getResultList();

        for (ProductEntity p : results) {
            p.getListOfAdditionalFeatures().size();
            p.getListOfPremium().size();
            p.getListOfAdditionalFeatures().size();
        }

        return results;
    }

    @Override
    public List<ProductEntity> filterProductsByCriteria(CategoryEnum category, boolean wantsRider, boolean isSmoker, BigDecimal sumAssured, Integer coverageTerm, Integer premiumTerm) throws InvalidFilterCriteriaException {
        if (category == null) {
            throw new InvalidFilterCriteriaException("Category is empty");
        }

        String categoryType = getCategoryEnumAsStringClass(category) + " p";

        String riderString = "";

        if (wantsRider) {
            riderString = "p.listOfRiders IS NOT EMPTY AND";
        } else if (!wantsRider) {
            riderString = "p.listOfRiders IS EMPTY AND";
        } else {
            riderString = "";
        }

        String smokerString = "";
        if (isSmoker) {
            smokerString = "AND s.isSmoker = true";
        } else {
            smokerString = "AND s.isSmoker = false";
        }

        Query query = em.createQuery("SELECT p FROM " + categoryType + " JOIN p.listOfPremium s WHERE " + riderString + "p.assuredSum >= :sumAssured AND p.isDeleted = FALSE AND p.coverageTerm >= :coverageTerm AND p.premiumTerm >= :premiumTerm " + smokerString);
        query.setParameter("sumAssured", sumAssured);
        query.setParameter("coverageTerm", coverageTerm);
        query.setParameter("premiumTerm", premiumTerm);

        List<ProductEntity> results = query.getResultList();

        for (ProductEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfAdditionalFeatures().size();
        }

        return results;
    }

    private String getCategoryEnumAsStringClass(CategoryEnum category) {
        String categoryType = "";

        switch (category) {
            case ENDOWMENT:
                categoryType = "EndowmentEntity";
                break;

//            case INVESTMENTLINKED:
//                categoryType = "InvestmentLinkedEntity";
//                break;

            case TERMLIFE:
                categoryType = "TermLifeProductEntity";
                break;

            case WHOLELIFE:
                categoryType = "WholeLifeProductEntity";
                break;

            default:
                break;
        }
        return categoryType;
    }

    @Override
    public ProductEntity createProductListing(ProductEntity newProduct, Long companyId) throws ProductAlreadyExistsException, UnknownPersistenceException, InvalidProductCreationException {
        Set<ConstraintViolation<ProductEntity>> productError = validator.validate(newProduct);
        if (productError.isEmpty()) {
            try {
                CompanyEntity company = em.find(CompanyEntity.class, companyId);
                newProduct.setCompany(company);
                company.getListOfProducts().add(newProduct);
                em.persist(newProduct);
                em.flush();

                newProduct.setCompany(company);
                return newProduct;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ProductAlreadyExistsException("Product already exists!");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InvalidProductCreationException(prepareInputDataValidationErrorsMessage(productError));
        }
    }

    @Override
    public void updateProductListing(ProductEntity updateProduct) throws ProductAlreadyExistsException, UnknownPersistenceException, InvalidProductCreationException {
        Set<ConstraintViolation<ProductEntity>> productError = validator.validate(updateProduct);
        if (productError.isEmpty()) {
            try {
                em.merge(updateProduct);
                em.flush();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ProductAlreadyExistsException("Product already exists!");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InvalidProductCreationException(prepareInputDataValidationErrorsMessage(productError));
        }
    }

    @Override
    public void deleteProductListing(Long productId) throws ProductNotFoundException {
        ProductEntity product = em.find(ProductEntity.class, productId);
        if (product == null) {
            throw new ProductNotFoundException("Product is not found!");
        } else {
            product.setIsDeleted(Boolean.TRUE);
        }
    }
    
    
    @Override
    public List<ProductEntity> retrieveListOfProductByCompany(String email) throws CompanyDoesNotExistException{
        CompanyEntity company = companySessionBean.retrieveCompanyByEmail(email);
        List<ProductEntity> listOfProducts = company.getListOfProducts();
        for(ProductEntity prod : listOfProducts){
            prod.getListOfAdditionalFeatures().size();
            prod.getListOfPremium().size();
            prod.getListOfAdditionalFeatures().size();
        }
        return listOfProducts;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ProductEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
