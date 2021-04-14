/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import ejb.entity.PaymentEntity;
import ejb.entity.ProductEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sohqi
 */
public class PaymentEntityWrapper {
    private List<PaymentEntity> listOfPayments;
    private BigInteger totalPayable;
 
   public PaymentEntityWrapper(){
       listOfPayments = new ArrayList<>();
   }
   public PaymentEntityWrapper(List<PaymentEntity> listOfPayment, BigInteger totalPayable){
       this();
       this.listOfPayments = listOfPayment;
       this.totalPayable = totalPayable;
   }

    public List<PaymentEntity> getListOfPayments() {
        return listOfPayments;
    }

    public void setListOfPayments(List<PaymentEntity> listOfPayments) {
        this.listOfPayments = listOfPayments;
    }

    public BigInteger getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(BigInteger totalPayable) {
        this.totalPayable = totalPayable;
    }

    
}
