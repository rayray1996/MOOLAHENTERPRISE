/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.CustomerEntity;
import ejb.entity.QuestionEntity;
import ejb.entity.QuestionnaireEntity;
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
import util.exception.CustomerDoesNotExistsException;
import util.exception.QuestionnaireErrorException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Stateless
public class QuestionnaireSessionBean implements QuestionnaireSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public QuestionnaireSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public QuestionnaireEntity createMyPreferences(QuestionnaireEntity newQuestion) throws UnknownPersistenceException, QuestionnaireErrorException {
        Set<ConstraintViolation<QuestionnaireEntity>> questionError = validator.validate(newQuestion);
        if (questionError.isEmpty()) {
            try {
                em.persist(newQuestion);
                em.flush();

                return newQuestion;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new QuestionnaireErrorException(ex.getMessage());
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new QuestionnaireErrorException(prepareInputDataValidationErrorsMessage(questionError));
        }

    }

    @Override
    public void updateMyPreferences(QuestionnaireEntity toUpdateQuestion) throws QuestionnaireErrorException, UnknownPersistenceException {
        Set<ConstraintViolation<QuestionnaireEntity>> questionError = validator.validate(toUpdateQuestion);
        if (questionError.isEmpty()) {
            try {
                em.merge(toUpdateQuestion);
                em.flush();

            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new QuestionnaireErrorException(ex.getMessage());
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new QuestionnaireErrorException(prepareInputDataValidationErrorsMessage(questionError));
        }
    }

    @Override
    public List<QuestionnaireEntity> retrieveAllQuestionnaire(Long custId) throws CustomerDoesNotExistsException {

        CustomerEntity cust = customerSessionBean.retrieveCustomerById(custId);

        List<QuestionnaireEntity> listOfQuestionnaire = cust.getListOfQuestionnaires();
        for (QuestionnaireEntity qn : listOfQuestionnaire) {
            qn.getListOfQuestions().size();
            for (QuestionEntity question : qn.getListOfQuestions()) {
                question.getListOfOptions().size();
            }
        }

        return listOfQuestionnaire;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<QuestionnaireEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
