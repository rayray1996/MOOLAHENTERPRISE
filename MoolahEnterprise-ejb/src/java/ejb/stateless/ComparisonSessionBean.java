/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.ComparisonEntity;
import ejb.entity.CustomerEntity;
import ejb.entity.ProductEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.ComparisonErrorException;
import util.exception.CustomerDoesNotExistsException;
import util.exception.QuestionnaireErrorException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Stateless
public class ComparisonSessionBean implements ComparisonSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ComparisonSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ComparisonEntity saveThisComparison(ComparisonEntity comparison) throws UnknownPersistenceException, ComparisonErrorException {
        Set<ConstraintViolation<ComparisonEntity>> comparisonError = validator.validate(comparison);
        if (comparisonError.isEmpty()) {
            try {
                em.persist(comparison);
                em.flush();

                return comparison;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ComparisonErrorException(ex.getMessage());
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new ComparisonErrorException(prepareInputDataValidationErrorsMessage(comparisonError));
        }
    }

    @Override
    public void updateThisComparison(ComparisonEntity comparison) throws UnknownPersistenceException, ComparisonErrorException {
        Set<ConstraintViolation<ComparisonEntity>> comparisonError = validator.validate(comparison);
        if (comparisonError.isEmpty()) {
            try {
                em.merge(comparison);
                em.flush();

            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ComparisonErrorException(ex.getMessage());
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new ComparisonErrorException(prepareInputDataValidationErrorsMessage(comparisonError));
        }
    }

    @Override
    public List<ComparisonEntity> viewSavedComparisonByCustId(Long custId) throws CustomerDoesNotExistsException {
        CustomerEntity cust = customerSessionBean.retrieveCustomerById(custId);
        List<ComparisonEntity> listOfComparison = cust.getSavedComparisons();
        for (ComparisonEntity compare : listOfComparison) {
            compare.getProductsToCompare().size();
            for (ProductEntity prod : compare.getProductsToCompare()) {
                prod.getListOfAdditionalFeatures().size();
                prod.getListOfPremium().size();
                prod.getListOfRiders().size();
                prod.getListOfSmokerPremium().size();
            }
        }
        return listOfComparison;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ComparisonEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
