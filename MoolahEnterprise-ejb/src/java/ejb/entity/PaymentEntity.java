/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Ada Wong
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class PaymentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Boolean paid;

    @Temporal(TemporalType.DATE)
    private Calendar dateTransacted; // This one can be null because havent pay then it is null

    private String paymentNumber;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Calendar dateGenerated; // This should not be null because it should indicate when it is generated

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(nullable = false)
    @NotNull
    private CompanyEntity company;

    public PaymentEntity() {
    }

    public PaymentEntity(Boolean paid, Calendar dateTransacted, String paymentNumber, Calendar dateGenerated, CompanyEntity company) {
        this.paid = paid;
        this.dateTransacted = dateTransacted;
        this.paymentNumber = paymentNumber;
        this.dateGenerated = dateGenerated;
        this.company = company;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Calendar getDateTransacted() {
        return dateTransacted;
    }

    public void setDateTransacted(Calendar dateTransacted) {
        this.dateTransacted = dateTransacted;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public Calendar getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(Calendar dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
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
        final PaymentEntity other = (PaymentEntity) obj;
        if (!Objects.equals(this.paymentNumber, other.paymentNumber)) {
            return false;
        }
        if (!Objects.equals(this.paymentId, other.paymentId)) {
            return false;
        }
        if (!Objects.equals(this.paid, other.paid)) {
            return false;
        }
        if (!Objects.equals(this.dateTransacted, other.dateTransacted)) {
            return false;
        }
        if (!Objects.equals(this.dateGenerated, other.dateGenerated)) {
            return false;
        }
        if (!Objects.equals(this.company, other.company)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PaymentEntity{" + "paymentId=" + paymentId + ", paid=" + paid + ", dateTransacted=" + dateTransacted + ", paymentNumber=" + paymentNumber + ", dateGenerated=" + dateGenerated + ", company=" + company + '}';
    }

    
    
}
