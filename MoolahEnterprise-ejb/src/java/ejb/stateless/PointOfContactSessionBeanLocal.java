/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.PointOfContactEntity;
import javax.ejb.Local;
import util.exception.InvalidPointOfContactCreationException;
import util.exception.PointOfContactAlreadyExistsException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rayta
 */
@Local
public interface PointOfContactSessionBeanLocal {

    public Long createNewPointOfContact(PointOfContactEntity poc) throws PointOfContactAlreadyExistsException, UnknownPersistenceException, InvalidPointOfContactCreationException;
    
}
