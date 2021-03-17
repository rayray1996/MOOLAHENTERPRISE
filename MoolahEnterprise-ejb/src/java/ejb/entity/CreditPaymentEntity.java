/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigInteger;
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

    public BigInteger getCreditPurchased() {
        return creditPurchased;
    }

    public void setCreditPurchased(BigInteger creditPurchased) {
        this.creditPurchased = creditPurchased;
    }
    
}
