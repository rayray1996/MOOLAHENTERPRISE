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
import util.enumeration.EndowmentProductEnum;
import util.enumeration.TermLifeProductEnum;
import util.enumeration.WholeLifeProductEnum;
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
        Query query = em.createQuery("SELECT p FROM ProductEntity p WHERE p.isDeleted = FALSE AND p.company.isDeleted = false AND p.company.isDeactivated = false");
        List<ProductEntity> results = query.getResultList();

        for (ProductEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfRiders().size();
        }
        return results;
    }

    @Override
    public List<EndowmentEntity> retrieveAllEndowmentProducts() {
        Query query = em.createQuery("SELECT e FROM EndowmentEntity e WHERE e.isDeleted = FALSE AND e.company.isDeleted = false AND e.company.isDeactivated = false");
        List<EndowmentEntity> results = query.getResultList();

        for (EndowmentEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfRiders().size();
        }

        return results;
    }

    @Override
    public List<TermLifeProductEntity> retrieveAllTermLifeProducts() {
        Query query = em.createQuery("SELECT t FROM TermLifeProductEntity t WHERE t.isDeleted = FALSE AND t.company.isDeleted = false AND t.company.isDeactivated = false");
        List<TermLifeProductEntity> results = query.getResultList();

        for (TermLifeProductEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfRiders().size();
        }
        return results;
    }

    @Override
    public List<WholeLifeProductEntity> retrieveAllWholeLifeProducts() {
        Query query = em.createQuery("SELECT w FROM WholeLifeProductEntity w WHERE w.isDeleted = FALSE AND w.company.isDeleted = false AND w.company.isDeactivated = false");
        List<WholeLifeProductEntity> results = query.getResultList();

        for (WholeLifeProductEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfRiders().size();
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
            product.getListOfRiders().size();
            return product;
        }
    }

    @Override
    public List<ProductEntity> searchForProductsByName(String name) {
        Query query = em.createQuery("SELECT p FROM ProductEntity p WHERE p.isDeleted = FALSE AND p.company.isDeleted = false AND p.company.isDeactivated = false AND p.productName LIKE :name");
        query.setParameter("name", "%" + name + "%");
        List<ProductEntity> results = query.getResultList();

        for (ProductEntity p : results) {
            p.getListOfAdditionalFeatures().size();
            p.getListOfPremium().size();
            p.getListOfRiders().size();
        }

        return results;
    }

    @Override
    public List<ProductEntity> filterProductsByCriteria(CategoryEnum category, boolean wantsRider, boolean isSmoker, BigDecimal sumAssured, Integer coverageTerm, Integer premiumTerm, EndowmentProductEnum endowmentProductEnum, TermLifeProductEnum termLifeProductEnum, WholeLifeProductEnum wholeLifeProductEnum) throws InvalidFilterCriteriaException {
        if (category == null) {
            throw new InvalidFilterCriteriaException("Category is empty");
        }

        String categoryType = getCategoryEnumAsStringClass(category) + " p";
        String endowmentType = getEndowmentEnumAsString(endowmentProductEnum);
        String termLifeType = getTermLifeEnumAsString(termLifeProductEnum);
        String wholeLifeType = getWholeLifeEnumAsString(wholeLifeProductEnum);

        String enumStrings = "";

        switch (category) {
            case ENDOWMENT:
                if (!(endowmentType.equals(""))) {
                    enumStrings = "AND p.productEnum = " + endowmentType;
                }
                break;

            case TERMLIFE:
                if (!(termLifeType.equals(""))) {
                    enumStrings = "AND p.productEnum = " + termLifeType;
                }
                break;

            case WHOLELIFE:
                if (!(wholeLifeType.equals(""))) {
                    enumStrings = "AND p.productEnum = " + wholeLifeType;
                }
                break;

            default:
                throw new InvalidFilterCriteriaException("Category is empty");
        }

        String riderString = "";

        if (wantsRider) {
            riderString = "p.listOfRiders IS NOT EMPTY AND";
        } else if (!wantsRider) {
            riderString = "p.listOfRiders IS EMPTY AND";
        } else {
            riderString = "";
        }

        Query query = em.createQuery("SELECT p FROM " + categoryType + " JOIN p.listOfPremium s WHERE " + riderString + "p.assuredSum >= :sumAssured AND p.isDeleted = FALSE AND p.company.isDeleted = false AND p.company.isDeactivated = false AND p.coverageTerm >= :coverageTerm AND p.premiumTerm >= :premiumTerm AND p.isSmoker = true AND " + enumStrings);
        query.setParameter("sumAssured", sumAssured);
        query.setParameter("coverageTerm", coverageTerm);
        query.setParameter("premiumTerm", premiumTerm);

        List<ProductEntity> results = query.getResultList();

        for (ProductEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfRiders().size();
        }

        return results;
    }

    private String getEndowmentEnumAsString(EndowmentProductEnum prodEnum) {
        String result = "";

        switch (prodEnum) {
            case ENDOWMENT:
                result = "ENDOWMENT";
                break;

            default:
                break;
        }

        return result;
    }

    private String getTermLifeEnumAsString(TermLifeProductEnum prodEnum) {
        String result = "";

        switch (prodEnum) {
            case ACCIDENT:
                result = "ACCIDENT";
                break;

            case CRITICALILLNESS:
                result = "CRITICALILLNESS";
                break;

            case HOSPITAL:
                result = "HOSPITAL";
                break;

            default:
                break;
        }

        return result;
    }

    private String getWholeLifeEnumAsString(WholeLifeProductEnum prodEnum) {
        String result = "";

        switch (prodEnum) {
            case ACCIDENT:
                result = "ACCIDENT";
                break;

            case CRITICALILLNESS:
                result = "CRITICALILLNESS";
                break;

            case HOSPITAL:
                result = "HOSPITAL";
                break;

            case LIFEINSURANCE:
                result = "LIFEINSURANCE";
                break;

            default:
                break;
        }

        return result;
    }

    private String getCategoryEnumAsStringClass(CategoryEnum category) {
        String categoryType = "";

        switch (category) {
            case ENDOWMENT:
                categoryType = "EndowmentEntity";
                break;

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
    public List<ProductEntity> retrieveListOfProductByCompany(String email) throws CompanyDoesNotExistException {
        CompanyEntity company = companySessionBean.retrieveCompanyByEmail(email);
        List<ProductEntity> listOfProducts = company.getListOfProducts();
        for (ProductEntity prod : listOfProducts) {
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
