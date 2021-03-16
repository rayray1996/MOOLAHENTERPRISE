/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.ComparisonEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ComparisonErrorException;
import util.exception.CustomerDoesNotExistsException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Local
public interface ComparisonSessionBeanLocal {

    public ComparisonEntity saveThisComparison(ComparisonEntity comparison) throws UnknownPersistenceException, ComparisonErrorException;

    public List<ComparisonEntity> viewSavedComparisonByCustId(Long custId) throws CustomerDoesNotExistsException;
    
}
