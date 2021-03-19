/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.CustomerEntity;
import ejb.entity.ProductEntity;
import ejb.entity.RefundEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerAlreadyExistException;
import util.exception.RefundCreationException;
import util.exception.RefundDoesNotExistException;
import util.exception.RefundErrorException;
import util.exception.RefundHasBeenTransactedException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Stateless
public class RefundSessionBean implements RefundSessionBeanLocal {

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RefundSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public RefundEntity createNewRefund(RefundEntity newRefund) throws UnknownPersistenceException, RefundCreationException, RefundErrorException {

        Set<ConstraintViolation<RefundEntity>> custError = validator.validate(newRefund);
        if (custError.isEmpty()) {
            try {
                em.persist(newRefund);
                em.flush();

                return newRefund;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new RefundErrorException(ex.getMessage());
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new RefundCreationException(prepareInputDataValidationErrorsMessage(custError));
        }
    }

    @Override
    public RefundEntity getRefund(Long refundId) throws RefundDoesNotExistException {
        try {
            RefundEntity refundEntity = (RefundEntity) em.createQuery("SELECT re FROM RefundEntity re WHERE re.refundId =:rId").setParameter("rId", refundId).getSingleResult();
            return refundEntity;
        } catch (NoResultException ex) {
            throw new RefundDoesNotExistException("Requested refund does not exist!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RefundEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
