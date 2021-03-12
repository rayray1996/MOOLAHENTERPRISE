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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 *
 * @author sohqi
 */
@Entity
public class ClickThroughEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clickThroughId;
    @Min(0)
    private BigInteger monthCounter;
    @Min(0)
    private BigInteger overallCounter;
    
    public Long getClickThroughId() {
        return clickThroughId;
    }

    public void setClickThroughId(Long clickThroughId) {
        this.clickThroughId = clickThroughId;
    }

    public BigInteger getMonthCounter() {
        return monthCounter;
    }

    public void setMonthCounter(BigInteger monthCounter) {
        this.monthCounter = monthCounter;
    }

    public BigInteger getOverallCounter() {
        return overallCounter;
    }

    public void setOverallCounter(BigInteger overallCounter) {
        this.overallCounter = overallCounter;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clickThroughId != null ? clickThroughId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the clickThroughId fields are not set
        if (!(object instanceof ClickThroughEntity)) {
            return false;
        }
        ClickThroughEntity other = (ClickThroughEntity) object;
        if ((this.clickThroughId == null && other.clickThroughId != null) || (this.clickThroughId != null && !this.clickThroughId.equals(other.clickThroughId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.ClickThroughEntity[ id=" + clickThroughId + " ]";
    }
    
}
