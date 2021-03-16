/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.IssueEntity;
import javax.ejb.Local;
import util.exception.InvalidIssueCreationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rayta
 */
@Local
public interface SystemAdminSessionBeanLocal {

    public Long createIssue(IssueEntity newIssue) throws InvalidIssueCreationException, UnknownPersistenceException;
    
}
