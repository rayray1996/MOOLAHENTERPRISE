/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.PremiumEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.PremiumAlreadyExistException;
import util.exception.PremiumCreationException;
import util.exception.PremiumDoesNotExistException;
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Local
public interface PremiumSessionBeanLocal {

    public List<PremiumEntity> retrieveListOfPremiumEntityForProduct(Long productId) throws ProductNotFoundException;

    public PremiumEntity retrievePremiumEntityById(Long premiumId) throws PremiumDoesNotExistException;

    public void deletePremium(Long premiumId) throws PremiumDoesNotExistException, ProductNotFoundException;

    public List<PremiumEntity> retrieveListOfSmokerPremiumEntityForProduct(Long productId) throws ProductNotFoundException;


}
