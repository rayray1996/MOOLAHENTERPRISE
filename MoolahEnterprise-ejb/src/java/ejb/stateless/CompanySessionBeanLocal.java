/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.CompanyEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Timer;
import util.exception.CompanyAlreadyExistException;
import util.exception.CompanyBeanValidaionException;
import util.exception.CompanyCreationException;
import util.exception.CompanyDoesNotExistException;
import util.exception.CompanySQLConstraintException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.MonthlyPaymentNotFoundException;
import util.exception.PointOfContactBeanValidationException;
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

    public void updateCompanyInformation(CompanyEntity company) throws UnknownPersistenceException, CompanySQLConstraintException;

    public void deactivateAccount(String email) throws CompanyDoesNotExistException;

    public void timeoutCleanUp(Timer timer);

    public void topupCredit(CompanyEntity company, BigInteger creditAmount) throws CompanyDoesNotExistException;

    public List<CompanyEntity> retrieveAllActiveCompanies();

    public void resetPassword(String email) throws CompanyDoesNotExistException;

    public List<MonthlyPaymentEntity> retrieveCurrentYearMonthlyPaymentEntity(Calendar year) throws MonthlyPaymentNotFoundException;

    public MonthlyPaymentEntity retrieveCurrentMonthlyPaymentEntity(Calendar month) throws MonthlyPaymentNotFoundException;

    public List<PaymentEntity> retrieveSpecificHistoricalTransactions(Calendar startDate, Calendar endDate);

    public List<PaymentEntity> retrieveAllHistoricalTransactions();

    public CompanyEntity createAccountForCompanyWS(CompanyEntity newCompany) throws CompanyAlreadyExistException, UnknownPersistenceException, CompanyCreationException, PointOfContactBeanValidationException;

    public void updateCompanyInformationWS(CompanyEntity company) throws UnknownPersistenceException, CompanySQLConstraintException, PointOfContactBeanValidationException, CompanyBeanValidaionException;
}
