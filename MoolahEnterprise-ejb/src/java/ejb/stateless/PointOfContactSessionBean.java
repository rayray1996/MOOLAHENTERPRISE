/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.CompanyEntity;
import ejb.entity.PointOfContactEntity;
import ejb.entity.ProductEntity;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InvalidPointOfContactCreationException;
import util.exception.InvalidProductCreationException;
import util.exception.PointOfContactAlreadyExistsException;
import util.exception.ProductAlreadyExistsException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rayta
 */
@Stateless
public class PointOfContactSessionBean implements PointOfContactSessionBeanLocal {

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public PointOfContactSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewPointOfContact(PointOfContactEntity poc) throws PointOfContactAlreadyExistsException, UnknownPersistenceException, InvalidPointOfContactCreationException {
        Set<ConstraintViolation<PointOfContactEntity>> pocError = validator.validate(poc);
        if (pocError.isEmpty()) {
            try {
                CompanyEntity company = em.find(CompanyEntity.class, poc.getCompany().getCompanyId());
                company.getListOfPointOfContacts().add(poc);
                em.persist(poc);
                em.flush();
                return poc.getPocId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new PointOfContactAlreadyExistsException("Point of Contact already exists!");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InvalidPointOfContactCreationException(prepareInputDataValidationErrorsMessage(pocError));
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PointOfContactEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
