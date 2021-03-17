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
import java.util.GregorianCalendar;
import java.util.List;
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
    private Long monthlyPaymentId;

    private Boolean paid;

    @Temporal(TemporalType.DATE)
    private GregorianCalendar dateTransacted; // This one can be null because havent pay then it is null

    private String paymentNumber;

    @NotNull
    @Min(value = 1)
    private BigInteger totalPayable;

    @Temporal(TemporalType.DATE)
    @NotNull
    private GregorianCalendar dateGenerated; // This should not be null because it should indicate when it is generated

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
    @JoinColumn(nullable = false)
    @NotNull
    private CompanyEntity company;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ProductLineItemEntity> listOfProductLineItems;

    public PaymentEntity() {
        this.listOfProductLineItems = new ArrayList<>();
    }

    public PaymentEntity(BigInteger totalPayable, GregorianCalendar dateGenerated, CompanyEntity company, List<ProductLineItemEntity> listOfProductLineItems) {
        this.totalPayable = totalPayable;
        this.dateGenerated = dateGenerated;
        this.company = company;
        this.listOfProductLineItems = listOfProductLineItems;
        this.paid = false;
        this.paymentNumber = "";
        this.dateTransacted = null;
    }

    public PaymentEntity(BigInteger totalPayable, GregorianCalendar dateGenerated, CompanyEntity company) {
        this();
        this.totalPayable = totalPayable;
        this.dateGenerated = dateGenerated;
        this.company = company;
        this.paid = false;
        this.paymentNumber = "";
        this.dateTransacted = null;
    }

    public PaymentEntity(Boolean paid, GregorianCalendar dateTransacted, String paymentNumber, BigInteger totalPayable, GregorianCalendar dateGenerated, CompanyEntity company, List<ProductLineItemEntity> listOfProductLineItems) {
        this.paid = paid;
        this.dateTransacted = dateTransacted;
        this.paymentNumber = paymentNumber;
        this.totalPayable = totalPayable;
        this.dateGenerated = dateGenerated;
        this.company = company;
        this.listOfProductLineItems = listOfProductLineItems;
    }

    public Long getMonthlyPaymentId() {
        return monthlyPaymentId;
    }

    public void setMonthlyPaymentId(Long monthlyPaymentId) {
        this.monthlyPaymentId = monthlyPaymentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (monthlyPaymentId != null ? monthlyPaymentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the monthlyPaymentId fields are not set
        if (!(object instanceof PaymentEntity)) {
            return false;
        }
        PaymentEntity other = (PaymentEntity) object;
        if ((this.monthlyPaymentId == null && other.monthlyPaymentId != null) || (this.monthlyPaymentId != null && !this.monthlyPaymentId.equals(other.monthlyPaymentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.MonthlyPaymentEntity[ id=" + monthlyPaymentId + " ]";
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public GregorianCalendar getDateTransacted() {
        return dateTransacted;
    }

    public void setDateTransacted(GregorianCalendar dateTransacted) {
        this.dateTransacted = dateTransacted;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public BigInteger getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(BigInteger totalPayable) {
        this.totalPayable = totalPayable;
    }

    public GregorianCalendar getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(GregorianCalendar dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public CompanyEntity getComapny() {
        return company;
    }

    public void setComapny(CompanyEntity company) {
        this.company = company;
    }

    public List<ProductLineItemEntity> getListOfProductLineItems() {
        return listOfProductLineItems;
    }

    public void setListOfProductLineItems(List<ProductLineItemEntity> listOfProductLineItems) {
        this.listOfProductLineItems = listOfProductLineItems;
    }

}
