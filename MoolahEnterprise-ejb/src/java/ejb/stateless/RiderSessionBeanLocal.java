/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.RiderEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ProductNotFoundException;
import util.exception.RiderAlreadyExistException;
import util.exception.RiderCreationException;
import util.exception.RiderDoesNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Local
public interface RiderSessionBeanLocal {

    public void deleteRider(Long riderId) throws RiderDoesNotExistException, ProductNotFoundException;

    public RiderEntity retrieveRiderByRiderID(Long riderId) throws RiderDoesNotExistException;

    public List<RiderEntity> retrieveListOfRiderEntityForProduct(Long productId) throws ProductNotFoundException;

}
