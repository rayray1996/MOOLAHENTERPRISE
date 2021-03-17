/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author rayta
 */
@Entity
public class MonthlyPaymentEntity extends PaymentEntity implements Serializable {

    public MonthlyPaymentEntity() {
    }

    public MonthlyPaymentEntity(Boolean paid, GregorianCalendar dateTransacted, String paymentNumber, BigInteger totalPayable, GregorianCalendar dateGenerated, CompanyEntity company, List<ProductLineItemEntity> listOfProductLineItems) {
        super(paid, dateTransacted, paymentNumber, totalPayable, dateGenerated, company, listOfProductLineItems);
    }

   
}
