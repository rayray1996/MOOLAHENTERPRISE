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
import javax.validation.constraints.NotNull;

/**
 *
 * @author Ada Wong
 */
@Entity
public class ProductLineItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodcutLineItemId;
    @NotNull
    private Long productId;
    @NotNull
    private BigInteger monthlyClicks;
    @NotNull
    private BigInteger monthlySubtotalCredit;
    @NotNull
    private BigInteger fixedSubscriptionCredit;

    public ProductLineItemEntity() {
    }

    public ProductLineItemEntity(Long productId, BigInteger monthlyClicks, BigInteger monthlySubtotalCredit, BigInteger fixedSubscriptionCredit) {
        this.productId = productId;
        this.monthlyClicks = monthlyClicks;
        this.monthlySubtotalCredit = monthlySubtotalCredit;
        this.fixedSubscriptionCredit = fixedSubscriptionCredit;
    }

    public Long getProdcutLineItemId() {
        return prodcutLineItemId;
    }

    public void setProdcutLineItemId(Long prodcutLineItemId) {
        this.prodcutLineItemId = prodcutLineItemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prodcutLineItemId != null ? prodcutLineItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the prodcutLineItemId fields are not set
        if (!(object instanceof ProductLineItemEntity)) {
            return false;
        }
        ProductLineItemEntity other = (ProductLineItemEntity) object;
        if ((this.prodcutLineItemId == null && other.prodcutLineItemId != null) || (this.prodcutLineItemId != null && !this.prodcutLineItemId.equals(other.prodcutLineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.ProductLineItemEntity[ id=" + prodcutLineItemId + " ]";
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigInteger getMonthlyClicks() {
        return monthlyClicks;
    }

    public void setMonthlyClicks(BigInteger monthlyClicks) {
        this.monthlyClicks = monthlyClicks;
    }

    public BigInteger getMonthlySubtotalCredit() {
        return monthlySubtotalCredit;
    }

    public void setMonthlySubtotalCredit(BigInteger monthlySubtotalCredit) {
        this.monthlySubtotalCredit = monthlySubtotalCredit;
    }

    public BigInteger getFixedSubscriptionCredit() {
        return fixedSubscriptionCredit;
    }

    public void setFixedSubscriptionCredit(BigInteger fixedSubscriptionCredit) {
        this.fixedSubscriptionCredit = fixedSubscriptionCredit;
    }

}
