/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helper;

import ejb.entity.PremiumEntity;
import ejb.entity.ProductEntity;
import java.io.Serializable;

/**
 *
 * @author rayta
 */
public class ProductEntityWrapper implements Serializable{

    private ProductEntity productEntity;
    private String stringParentCategory; // main category - wholelife, termlife, endowment
    private String stringChildCategory; // specific category for each main category -- accident, hospital etc...
    private PremiumEntity smokerPremium;
    private PremiumEntity normalPremium;
    
    public ProductEntityWrapper() {
    }

    public ProductEntityWrapper(ProductEntity productEntity, String stringParentCategory, String stringChildCategory) {
        this.productEntity = productEntity;
        this.stringParentCategory = stringParentCategory;
        this.stringChildCategory = stringChildCategory;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public String getStringParentCategory() {
        return stringParentCategory;
    }

    public void setStringParentCategory(String stringParentCategory) {
        this.stringParentCategory = stringParentCategory;
    }

    public String getStringChildCategory() {
        return stringChildCategory;
    }

    public void setStringChildCategory(String stringChildCategory) {
        this.stringChildCategory = stringChildCategory;
    }

    @Override
    public String toString() {
        return "ProductEntityWrapper{" + "productEntity=" + productEntity + ", stringParentCategory=" + stringParentCategory + ", stringChildCategory=" + stringChildCategory + '}';
    }

    public PremiumEntity getSmokerPremium() {
        return smokerPremium;
    }

    public void setSmokerPremium(PremiumEntity smokerPremium) {
        this.smokerPremium = smokerPremium;
    }

    public PremiumEntity getNormalPremium() {
        return normalPremium;
    }

    public void setNormalPremium(PremiumEntity normalPremium) {
        this.normalPremium = normalPremium;
    }

    
    
}
