/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rayta
 */
@Entity
public class RefundEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refundId;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar refundDate;
    
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalAmount;
    
    @OneToOne
    @JoinColumn(nullable = false)
    private CompanyEntity company;

    public RefundEntity() {
    }
    
    public RefundEntity(Calendar refundDate, BigDecimal totalAmount, CompanyEntity company) {
        this.refundDate = refundDate;
        this.totalAmount = totalAmount;
        this.company = company;
    }

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public Calendar getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Calendar refundDate) {
        this.refundDate = refundDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RefundEntity other = (RefundEntity) obj;
        if (!Objects.equals(this.refundId, other.refundId)) {
            return false;
        }
        if (!Objects.equals(this.refundDate, other.refundDate)) {
            return false;
        }
        if (!Objects.equals(this.totalAmount, other.totalAmount)) {
            return false;
        }
        if (!Objects.equals(this.company, other.company)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RefundEntity{" + "refundId=" + refundId + ", refundDate=" + refundDate + ", totalAmount=" + totalAmount + ", company=" + company + '}';
    }
    
}
