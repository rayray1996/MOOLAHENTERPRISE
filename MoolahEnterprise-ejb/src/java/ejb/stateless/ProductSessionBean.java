/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.ClickThroughEntity;
import ejb.entity.CompanyEntity;
import ejb.entity.EndowmentEntity;
import ejb.entity.FeatureEntity;
import ejb.entity.PremiumEntity;
import ejb.entity.ProductEntity;
import ejb.entity.RiderEntity;
import ejb.entity.TermLifeProductEntity;
import ejb.entity.WholeLifeProductEntity;
import java.math.BigDecimal;
import java.util.HashSet;
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
import jdk.nashorn.internal.ir.BreakableNode;
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
            e.getListOfSmokerPremium().size();
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
            e.getListOfSmokerPremium().size();
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
            e.getListOfSmokerPremium().size();
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
            e.getListOfSmokerPremium().size();
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
            product.getListOfSmokerPremium().size();
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
            p.getListOfSmokerPremium().size();
        }

        return results;
    }

    @Override
    public List<ProductEntity> filterProductsByCriteria(CategoryEnum category, Boolean wantsRider, Boolean isSmoker, BigDecimal sumAssured, Integer coverageTerm, Integer premiumTerm, EndowmentProductEnum endowmentProductEnum, TermLifeProductEnum termLifeProductEnum, WholeLifeProductEnum wholeLifeProductEnum) throws InvalidFilterCriteriaException {
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

        if (wantsRider == null) {
            riderString = "p.listOfRiders IS EMPTY OR p.listOfRiders IS NOT EMPTY";
        } else if (!wantsRider) {
            riderString = "p.listOfRiders IS EMPTY";
        } else {
            riderString = "p.listOfRiders IS NOT EMPTY";

        }

        // smoker default to no
        String smokerString = "";
        if (isSmoker) {
            smokerString = "p.listOfSmokerPremium IS NOT EMPTY";
        } else {
            smokerString = "p.listOfSmokerPremium IS EMPTY OR p.listOfSmokerPremium IS NOT EMPTY";
        }

        // coverage term default to -1 (no preference)
        String coverageTermString = "";
        if (coverageTerm < 0) {
            coverageTermString = "p.coverageTerm >= 0";
        } else {
            coverageTermString = ":coverageTerm <= p.coverageTerm";
        }

        // premium term default to -1 (no prefernce)
        String premiumTermString = "";
        if (premiumTerm < 0) {
            premiumTermString = "p.premiumTerm >= 0";
        } else {
            premiumTermString = ":premiumTerm <= p.premiumTerm";
        }

        // sumAssured is greater than or equal
        // sumAssured default to -1
        String sumAssuredString = "";
        if (sumAssured.compareTo(BigDecimal.ZERO) < 0) {
            sumAssuredString = "p.assuredSum >= 0";
        } else {
            sumAssuredString = ":sumAssured >= p.assuredSum";
        }

        Query query = em.createQuery("SELECT p FROM " + categoryType + " JOIN p.listOfPremium s WHERE p.productId IS DISTINCT AND p.isDeleted = FALSE AND p.company.isDeleted = false AND p.company.isDeactivated = false" + " AND "
                + riderString + " AND " + smokerString + " AND " + coverageTermString + " AND " + premiumTermString + " AND " + sumAssuredString);
        if(coverageTerm >= 0) {
        query.setParameter("coverageTerm", coverageTerm);
        }
        if(premiumTerm >= 0) {
        query.setParameter("premiumTerm", premiumTerm);
        }
        if(sumAssured.compareTo(BigDecimal.ZERO) >= 0) {
        query.setParameter("sumAssured", sumAssured);
        }
        List<ProductEntity> results = query.getResultList();

        for (ProductEntity e : results) {
            e.getListOfAdditionalFeatures().size();
            e.getListOfPremium().size();
            e.getListOfRiders().size();
            e.getListOfSmokerPremium().size();
        }

        return results;
    }

    private String getEndowmentEnumAsString(EndowmentProductEnum prodEnum) {
        String result = "";
        if (prodEnum == null) {
            return result;
        }
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
        if (prodEnum == null) {
            return result;
        }
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
        if (prodEnum == null) {
            return result;
        }
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
        if (category == null) {
            return categoryType;
        }
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
    public ProductEntity createProductListing(ProductEntity newProduct, Long companyId, List<RiderEntity> riders, List<PremiumEntity> premiums, List<PremiumEntity> smokerPremiums, List<FeatureEntity> features) throws ProductAlreadyExistsException, UnknownPersistenceException, InvalidProductCreationException {
        Set<ConstraintViolation<ProductEntity>> productError = validator.validate(newProduct);
        Set<ConstraintViolation<RiderEntity>> riderError = new HashSet<>();
        Set<ConstraintViolation<PremiumEntity>> premiumError = new HashSet<>();
        Set<ConstraintViolation<FeatureEntity>> featureError = new HashSet<>();

        // validate riders
        for (RiderEntity r : riders) {
            riderError = validator.validate(r);
            if (!riderError.isEmpty()) {
                break;
            } else {
                newProduct.getListOfRiders().add(r);
            }
        }

        // validate premiums
        for (PremiumEntity p : premiums) {
            premiumError = validator.validate(p);
            em.persist(p);
            em.flush();
            if (!premiumError.isEmpty()) {
                break;
            } else {
                newProduct.getListOfPremium().add(p);
            }
        }

        for (PremiumEntity p : smokerPremiums) {
            premiumError = validator.validate(p);
            em.persist(p);
            em.flush();
            if (!premiumError.isEmpty()) {
                break;
            } else {
                newProduct.getListOfSmokerPremium().add(p);
            }
        }

        for (FeatureEntity f : features) {
            featureError = validator.validate(f);
            if (!featureError.isEmpty()) {
                break;
            } else {
                newProduct.getListOfAdditionalFeatures().add(f);
            }
        }

        if (productError.isEmpty() && riderError.isEmpty() && featureError.isEmpty() && premiumError.isEmpty()) {
            try {
                CompanyEntity company = em.find(CompanyEntity.class, companyId);
                newProduct.setCompany(company);
                ClickThroughEntity clickThrough = new ClickThroughEntity();
                newProduct.setClickThroughInfo(clickThrough);
                em.persist(newProduct);
                em.flush();

                company.getListOfProducts().add(newProduct);
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
            throw new InvalidProductCreationException(prepareInputDataValidationErrorsMessage(productError)
                    + "\n" + prepareInputDataValidationErrorsMessage(riderError)
                    + "\n" + prepareInputDataValidationErrorsMessage(premiumError)
                    + "\n" + prepareInputDataValidationErrorsMessage(featureError));
        }
    }

    @Override
    public void updateProductListing(ProductEntity updateProduct) throws ProductAlreadyExistsException, UnknownPersistenceException, InvalidProductCreationException {
        Set<ConstraintViolation<ProductEntity>> productError = validator.validate(updateProduct);
        Set<ConstraintViolation<RiderEntity>> riderError = new HashSet<>();
        Set<ConstraintViolation<PremiumEntity>> premiumError = new HashSet<>();
        Set<ConstraintViolation<FeatureEntity>> featureError = new HashSet<>();

        for (RiderEntity r : updateProduct.getListOfRiders()) {
            riderError = validator.validate(r);
            if (!riderError.isEmpty()) {
                break;
            }
        }

        // validate premiums
        for (PremiumEntity p : updateProduct.getListOfPremium()) {
            premiumError = validator.validate(p);
            if (!premiumError.isEmpty()) {
                break;
            }
        }

        for (PremiumEntity p : updateProduct.getListOfSmokerPremium()) {
            premiumError = validator.validate(p);
            if (!premiumError.isEmpty()) {
                break;
            }
        }

        for (FeatureEntity f : updateProduct.getListOfAdditionalFeatures()) {
            featureError = validator.validate(f);
            if (!featureError.isEmpty()) {
                break;
            }
        }

        if (productError.isEmpty() && riderError.isEmpty() && featureError.isEmpty() && premiumError.isEmpty()) {
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
            throw new InvalidProductCreationException(prepareInputDataValidationErrorsMessage(productError)
                    + "\n" + prepareInputDataValidationErrorsMessage(riderError)
                    + "\n" + prepareInputDataValidationErrorsMessage(premiumError)
                    + "\n" + prepareInputDataValidationErrorsMessage(featureError));
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
            prod.getListOfSmokerPremium().size();
        }
        return listOfProducts;
    }

    private <T> String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<T>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
