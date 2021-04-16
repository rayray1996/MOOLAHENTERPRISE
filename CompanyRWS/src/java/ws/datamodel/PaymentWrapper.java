/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import ejb.entity.CreditPaymentEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.ProductEntity;
import ejb.entity.ProductLineItemEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sohqi
 */
public class PaymentWrapper {
    //cater to all
    private PaymentEntity paymentEntity;
    //cater to monthly
    private BigInteger totalPayable;
    private List<ProductLineItemEntity> listOfProductLineItemEntity;
    //cater to credit
    private CreditPaymentEntity creditPaymentEntity;
    

    public PaymentWrapper() {
        paymentEntity = new PaymentEntity();
        listOfProductLineItemEntity = new ArrayList<>();
        creditPaymentEntity = new CreditPaymentEntity();
    }

    public PaymentWrapper(PaymentEntity paymentEntity, BigInteger totalPayable, List<ProductLineItemEntity> listOfProductLineItemEntity, CreditPaymentEntity creditPaymentEntity) {
        this();
        this.listOfProductLineItemEntity = listOfProductLineItemEntity;
        this.totalPayable = totalPayable;
        this.paymentEntity = paymentEntity;
        this.creditPaymentEntity = creditPaymentEntity;
    }

    public BigInteger getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(BigInteger totalPayable) {
        this.totalPayable = totalPayable;
    }

    public PaymentEntity getPaymentEntity() {
        return paymentEntity;
    }

    public void setPaymentEntity(PaymentEntity paymentEntity) {
        this.paymentEntity = paymentEntity;
    }

    public List<ProductLineItemEntity> getListOfProductLineItemEntity() {
        return listOfProductLineItemEntity;
    }

    public void setListOfProductLineItemEntity(List<ProductLineItemEntity> listOfProductLineItemEntity) {
        this.listOfProductLineItemEntity = listOfProductLineItemEntity;
    }

    public CreditPaymentEntity getCreditPaymentEntity() {
        return creditPaymentEntity;
    }

    public void setCreditPaymentEntity(CreditPaymentEntity creditPaymentEntity) {
        this.creditPaymentEntity = creditPaymentEntity;
    }

}
