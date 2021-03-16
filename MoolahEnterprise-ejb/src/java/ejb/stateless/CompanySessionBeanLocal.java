/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.CompanyEntity;
import javax.ejb.Local;
import util.exception.CompanyAlreadyExistException;
import util.exception.CompanyCreationException;
import util.exception.CompanyDoesNotExistException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Local
public interface CompanySessionBeanLocal {

    public CompanyEntity createAccountForCompany(CompanyEntity newCompany) throws CompanyAlreadyExistException, UnknownPersistenceException, CompanyCreationException;

    public CompanyEntity login(String companyEmail, String password) throws CompanyDoesNotExistException, IncorrectLoginParticularsException;

    public CompanyEntity retrieveCompanyByEmail(String email) throws CompanyDoesNotExistException;

    public void updateCompanyInformation(CompanyEntity company) throws UnknownPersistenceException, CompanyDoesNotExistException;
    
}
