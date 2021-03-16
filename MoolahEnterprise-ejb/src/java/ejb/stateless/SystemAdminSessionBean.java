/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.IssueEntity;
import java.util.GregorianCalendar;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InvalidIssueCreationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rayta
 */
@Stateless
public class SystemAdminSessionBean implements SystemAdminSessionBeanLocal {

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public SystemAdminSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public Long createIssue(IssueEntity newIssue) throws InvalidIssueCreationException, UnknownPersistenceException {
        newIssue.setDateOfIssue(new GregorianCalendar());

        Set<ConstraintViolation<IssueEntity>> issueError = validator.validate(newIssue);
        if (issueError.isEmpty()) {
            try {
                em.persist(newIssue);
                em.flush();

                return newIssue.getIssueId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new InvalidIssueCreationException("Issue ID already exists");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            if (newIssue.getReasonForContact() == null) {
                throw new InvalidIssueCreationException("Reason for Contact is empty");
            } else if (newIssue.getIssueDescription() == null) {
                throw new InvalidIssueCreationException("Issue Description is empty");
            }
        }
        throw new InvalidIssueCreationException(prepareInputDataValidationErrorsMessage(issueError));
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<IssueEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
