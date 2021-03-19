/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.RefundEntity;
import javax.ejb.Local;
import util.exception.RefundCreationException;
import util.exception.RefundDoesNotExistException;
import util.exception.RefundErrorException;
import util.exception.RefundHasBeenTransactedException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Local
public interface RefundSessionBeanLocal {

    public RefundEntity getRefund(Long refundId) throws RefundDoesNotExistException;

    public RefundEntity createNewRefund(RefundEntity newRefund) throws UnknownPersistenceException, RefundCreationException, RefundErrorException;
    
}
