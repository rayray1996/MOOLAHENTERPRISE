/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.PolicyCurrencyEnum;

/**
 *
 * @author sohqi
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ProductEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @Lob
    @Column(name = "product_Image")
    private byte[] productImage;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar productDateCreated;
    @NotNull
    @Size(min=1)
    private String productName;
    @NotNull
    @Min(1)
    private Integer coverageTerm;
    @NotNull
    @Digits(integer=10, fraction=3)
    private BigDecimal assuredSum;
    @NotNull
    @Size(min=1)
    private String description;
    @NotNull
    private Boolean isDeleted;
    @NotNull
    private Integer premiumTerm;
    @NotNull
    @Enumerated
    private PolicyCurrencyEnum policyCurrency;
    @OneToMany (cascade = {CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    private List<FeatureEntity> listOfAdditionalFeatures;
    @OneToMany (cascade = {CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    private List<RiderEntity> listOfRiders;
    @OneToOne
    private ClickThroughEntity clickThroughInfo;
    @ManyToOne 
    private CategoryPricingEntity productCategoryPricing;
    @ManyToOne
    private CompanyEntity company;
    @OneToMany (cascade = {CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    private List<PremiumEntity> listOfPremium;

    public ProductEntity() {
        listOfAdditionalFeatures = new ArrayList<FeatureEntity>();
        listOfRiders = new ArrayList<RiderEntity>();
        clickThroughInfo = new ClickThroughEntity();
        productCategoryPricing = new CategoryPricingEntity ();
        company = new CompanyEntity();
        listOfPremium  = new ArrayList<PremiumEntity>();
        productImage = null;
    }

    public ProductEntity(String productName, Integer coverageTerm, BigDecimal assuredSum, String description, Boolean isDeleted, Integer premiumTerm, PolicyCurrencyEnum currency) {
        this();
        this.productName = productName;
        this.coverageTerm = coverageTerm;
        this.assuredSum = assuredSum;
        this.description = description;
        this.isDeleted = isDeleted;
        this.premiumTerm = premiumTerm;
        this.productDateCreated = new GregorianCalendar();
        this.policyCurrency = currency;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCoverageTerm() {
        return coverageTerm;
    }

    public void setCoverageTerm(Integer coverageTerm) {
        this.coverageTerm = coverageTerm;
    }

    public Integer getPremiumTerm() {
        return premiumTerm;
    }

    public void setPremiumTerm(Integer premiumTerm) {
        this.premiumTerm = premiumTerm;
    }


    public BigDecimal getAssuredSum() {
        return assuredSum;
    }

    public void setAssuredSum(BigDecimal assuredSum) {
        this.assuredSum = assuredSum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public PolicyCurrencyEnum getPolicyCurrency() {
        return policyCurrency;
    }

    public void setPolicyCurrency(PolicyCurrencyEnum policyCurrency) {
        this.policyCurrency = policyCurrency;
    }

    public List<FeatureEntity> getListOfAdditionalFeatures() {
        return listOfAdditionalFeatures;
    }

    public void setListOfAdditionalFeatures(List<FeatureEntity> listOfAdditionalFeatures) {
        this.listOfAdditionalFeatures = listOfAdditionalFeatures;
    }

    public List<RiderEntity> getListOfRiders() {
        return listOfRiders;
    }

    public void setListOfRiders(List<RiderEntity> listOfRiders) {
        this.listOfRiders = listOfRiders;
    }

    public ClickThroughEntity getClickThroughInfo() {
        return clickThroughInfo;
    }

    public void setClickThroughInfo(ClickThroughEntity clickThroughInfo) {
        this.clickThroughInfo = clickThroughInfo;
    }

    public CategoryPricingEntity getProductCategoryPricing() {
        return productCategoryPricing;
    }

    public void setProductCategoryPricing(CategoryPricingEntity productCategoryPricing) {
        this.productCategoryPricing = productCategoryPricing;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    public List<PremiumEntity> getListOfPremium() {
        return listOfPremium;
    }

    public void setListOfPremium(List<PremiumEntity> listOfPremium) {
        this.listOfPremium = listOfPremium;
    }
    
    
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public Calendar getProductDateCreated() {
        return productDateCreated;
    }

    public void setProductDateCreated(Calendar productDateCreated) {
        this.productDateCreated = productDateCreated;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the productId fields are not set
        if (!(object instanceof ProductEntity)) {
            return false;
        }
        ProductEntity other = (ProductEntity) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.ProductEntity[ id=" + productId + " ]";
    }
    
}
