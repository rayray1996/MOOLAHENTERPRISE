/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.QuestionnaireEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerDoesNotExistsException;
import util.exception.QuestionnaireErrorException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Local
public interface QuestionnaireSessionBeanLocal {

    public List<QuestionnaireEntity> retrieveAllQuestionnaire(Long custId) throws CustomerDoesNotExistsException;

    public void updateMyPreferences(QuestionnaireEntity toUpdateQuestion) throws QuestionnaireErrorException, UnknownPersistenceException;

    public QuestionnaireEntity createMyPreferences(QuestionnaireEntity newQuestion) throws UnknownPersistenceException, QuestionnaireErrorException;
    
}
