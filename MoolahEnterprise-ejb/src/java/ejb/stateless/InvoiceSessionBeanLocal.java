/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import util.exception.ProductLineItemException;
import ejb.entity.ProductLineItemEntity;
import java.math.BigInteger;
import javax.ejb.Local;
import util.exception.CompanyDoesNotExistException;
import util.exception.InvalidPaymentEntityCreationException;
import util.exception.MonthlyPaymentAlreadyExistsException;
import util.exception.MonthlyPaymentException;
import util.exception.MonthlyPaymentNotFoundException;
import util.exception.PaymentEntityAlreadyExistsException;
import util.exception.ProductLineItemAlreadyExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rayta
 */
@Local
public interface InvoiceSessionBeanLocal {

    public ProductLineItemEntity createProductLineItem(ProductLineItemEntity newProductLineItem) throws ProductLineItemAlreadyExistException, UnknownPersistenceException, ProductLineItemException;

    public MonthlyPaymentEntity createMonthlyPayment(MonthlyPaymentEntity newMonthlyPayment) throws MonthlyPaymentAlreadyExistsException, UnknownPersistenceException, MonthlyPaymentException;

    public void automatedMonthlyInvoice();

    public void makePayment(Long paymentId, Long companyId) throws MonthlyPaymentNotFoundException, CompanyDoesNotExistException;

    public Long purchaseMoolahCredits(Long companyId, BigInteger creditToBuy) throws CompanyDoesNotExistException, InvalidPaymentEntityCreationException, InvalidPaymentEntityCreationException, UnknownPersistenceException, PaymentEntityAlreadyExistsException;

    public MonthlyPaymentEntity retrieveMonthlyPaymentById(Long monthlyPaymentId) throws MonthlyPaymentNotFoundException;

}
