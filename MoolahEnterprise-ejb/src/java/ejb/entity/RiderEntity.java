/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author sohqi
 */
@Entity
public class RiderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long riderId;
    @NotNull
    @Digits(integer=10, fraction=3)
    private BigDecimal riderPremiumValue;
    @NotNull
    @Size(min=1)
    private String riderDescription;

    public BigDecimal getRiderPremiumValue() {
        return riderPremiumValue;
    }

    public void setRiderPremiumValue(BigDecimal riderPremiumValue) {
        this.riderPremiumValue = riderPremiumValue;
    }

    public String getRiderDescription() {
        return riderDescription;
    }

    public void setRiderDescription(String riderDescription) {
        this.riderDescription = riderDescription;
    }
    
    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (riderId != null ? riderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the riderId fields are not set
        if (!(object instanceof RiderEntity)) {
            return false;
        }
        RiderEntity other = (RiderEntity) object;
        if ((this.riderId == null && other.riderId != null) || (this.riderId != null && !this.riderId.equals(other.riderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.RiderEntity[ id=" + riderId + " ]";
    }
    
}
