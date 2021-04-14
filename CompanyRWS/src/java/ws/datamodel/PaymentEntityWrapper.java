/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

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
public class PaymentEntityWrapper {

    private PaymentEntity paymentEntity;
    private BigInteger totalPayable;
    private List<ProductLineItemEntity> listOfProductLineItemEntity;

    public PaymentEntityWrapper() {
        paymentEntity = new PaymentEntity();
        listOfProductLineItemEntity = new ArrayList<>();

    }

    public PaymentEntityWrapper(PaymentEntity paymentEntity, BigInteger totalPayable, List<ProductLineItemEntity> listOfProductLineItemEntity) {
        this();
        this.listOfProductLineItemEntity = listOfProductLineItemEntity;
        this.totalPayable = totalPayable;
        this.paymentEntity = paymentEntity;
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

}
