/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.FeatureEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.FeatureAlreadyExistsException;
import util.exception.FeatureCreationException;
import util.exception.FeatureDoesNotExistsException;
import util.exception.ProductIsDeletedException;
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Local
public interface FeatureSessionBeanLocal {

    public List<FeatureEntity> retrieveListOfFeatures(Long productId) throws ProductNotFoundException, ProductIsDeletedException;

    public FeatureEntity retrieveFeatureEntitybyId(Long featureId) throws FeatureDoesNotExistsException;

    public void deleteFeatureEntity(Long featureId) throws FeatureDoesNotExistsException, ProductNotFoundException;

}
