/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rayta
 */
@Entity
public class CreditPaymentEntity extends PaymentEntity implements Serializable {

    @NotNull
    @Min(0)
    private BigInteger creditPurchased;
    
    
    private BigDecimal totalPayable;

    public CreditPaymentEntity() {
    }

    public CreditPaymentEntity(BigInteger creditPurchased, Boolean paid, Calendar dateTransacted, String paymentNumber, BigDecimal totalPayable, Calendar dateGenerated, CompanyEntity company) {
        super(paid, dateTransacted, paymentNumber, dateGenerated, company);
        this.creditPurchased = creditPurchased;
        this.totalPayable = totalPayable;
    }

    public BigInteger getCreditPurchased() {
        return creditPurchased;
    }

    public void setCreditPurchased(BigInteger creditPurchased) {
        this.creditPurchased = creditPurchased;
    }

    public BigDecimal getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(BigDecimal totalPayable) {
        this.totalPayable = totalPayable;
    }

    @Override
    public String toString() {
        return "CreditPaymentEntity{" + "creditPurchased=" + creditPurchased + ", totalPayable=" + totalPayable + '}';
    }
}
