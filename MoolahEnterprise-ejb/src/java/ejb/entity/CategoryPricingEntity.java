/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.CategoryEnum;

/**
 *
 * @author sohqi
 */
@Entity
public class CategoryPricingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryPricingId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CategoryEnum categoryType;
    @Min(1)
    @NotNull
    private BigInteger fixedSubscriptionCredit;
    @Min(1)
    @NotNull
    private BigInteger creditPerClick;

    public CategoryPricingEntity() {
    }

    public CategoryPricingEntity(CategoryEnum categoryType, BigInteger fixedSubscriptionCredit, BigInteger creditPerClick) {
        this.categoryType = categoryType;
        this.fixedSubscriptionCredit = fixedSubscriptionCredit;
        this.creditPerClick = creditPerClick;
    }

    public CategoryEnum getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryEnum categoryType) {
        this.categoryType = categoryType;
    }

    public BigInteger getFixedSubscriptionCredit() {
        return fixedSubscriptionCredit;
    }

    public void setFixedSubscriptionCredit(BigInteger fixedSubscriptionCredit) {
        this.fixedSubscriptionCredit = fixedSubscriptionCredit;
    }

    public BigInteger getCreditPerClick() {
        return creditPerClick;
    }

    public void setCreditPerClick(BigInteger creditPerClick) {
        this.creditPerClick = creditPerClick;
    }

    public Long getCategoryPricingId() {
        return categoryPricingId;
    }

    public void setCategoryPricingId(Long categoryPricingId) {
        this.categoryPricingId = categoryPricingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryPricingId != null ? categoryPricingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the categoryPricingId fields are not set
        if (!(object instanceof CategoryPricingEntity)) {
            return false;
        }
        CategoryPricingEntity other = (CategoryPricingEntity) object;
        if ((this.categoryPricingId == null && other.categoryPricingId != null) || (this.categoryPricingId != null && !this.categoryPricingId.equals(other.categoryPricingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.CategoryPricingEntity[ id=" + categoryPricingId + " ]";
    }

}
