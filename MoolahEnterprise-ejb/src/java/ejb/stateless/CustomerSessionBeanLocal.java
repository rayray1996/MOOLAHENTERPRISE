/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.CustomerEntity;
import javax.ejb.Local;
import util.exception.CustomerAlreadyExistException;
import util.exception.CustomerCreationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Local
public interface CustomerSessionBeanLocal {

    public Long createCustomer(CustomerEntity newCust) throws CustomerAlreadyExistException, UnknownPersistenceException, CustomerCreationException;

}
