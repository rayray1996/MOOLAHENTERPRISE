/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.MonthlyPaymentEntity;
import util.exception.ProductLineItemException;
import ejb.entity.ProductLineItemEntity;
import javax.ejb.Local;
import util.exception.MonthlyPaymentAlreadyExistsException;
import util.exception.MonthlyPaymentException;
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
    
}
