/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nickg
 */
@Entity
public class AssetEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetId;
    
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal cashInHand;
    
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal monthlyExpense;
    
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal investments;
    
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal monthlyIncome;
    
    @OneToOne(mappedBy = "asset", optional = false)
    private CustomerEntity customer;

    public AssetEntity() {
    }

    public AssetEntity(BigDecimal cashInHand, BigDecimal monthlyExpense, BigDecimal investments, CustomerEntity customer) {
        this.cashInHand = cashInHand;
        this.monthlyExpense = monthlyExpense;
        this.investments = investments;
        this.customer = customer;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public BigDecimal getCashInHand() {
        return cashInHand;
    }

    public BigDecimal getMonthlyExpense() {
        return monthlyExpense;
    }

    public void setMonthlyExpense(BigDecimal monthlyExpense) {
        this.monthlyExpense = monthlyExpense;
    }

    public BigDecimal getInvestments() {
        return investments;
    }

    public void setInvestments(BigDecimal investments) {
        this.investments = investments;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assetId != null ? assetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the assetId fields are not set
        if (!(object instanceof AssetEntity)) {
            return false;
        }
        AssetEntity other = (AssetEntity) object;
        if ((this.assetId == null && other.assetId != null) || (this.assetId != null && !this.assetId.equals(other.assetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.AssetEntity[ id=" + assetId + " ]";
    }
    
}
