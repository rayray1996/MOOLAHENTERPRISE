/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.ComparisonEntity;
import javax.ejb.Local;
import util.exception.ComparisonErrorException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Local
public interface ComparisonSessionBeanLocal {

    public ComparisonEntity saveThisComparison(ComparisonEntity comparison) throws UnknownPersistenceException, ComparisonErrorException;
    
}
