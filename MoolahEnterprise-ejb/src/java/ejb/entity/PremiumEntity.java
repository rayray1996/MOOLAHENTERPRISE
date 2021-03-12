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
import javax.validation.constraints.Min;

/**
 *
 * @author sohqi
 */
@Entity
public class PremiumEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long premiumId;
    @Min(1)
    private Integer minAgeGroup;
    @Min(1)
    private Integer maxAgeGroup;
    @Digits(integer=10, fraction=3)
    private BigDecimal value;
    private boolean isSmoker;
    @Digits(integer=10, fraction=3)
    private BigDecimal guaranteeSum;
    
    
    public Long getPremiumId() {
        return premiumId;
    }

    public void setPremiumId(Long premiumId) {
        this.premiumId = premiumId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (premiumId != null ? premiumId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the premiumId fields are not set
        if (!(object instanceof PremiumEntity)) {
            return false;
        }
        PremiumEntity other = (PremiumEntity) object;
        if ((this.premiumId == null && other.premiumId != null) || (this.premiumId != null && !this.premiumId.equals(other.premiumId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.PremiumEntity[ id=" + premiumId + " ]";
    }
    
}
