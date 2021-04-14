/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import ejb.entity.FeatureEntity;
import ejb.entity.PremiumEntity;
import ejb.entity.ProductEntity;
import ejb.entity.RiderEntity;
import java.math.BigDecimal;
import java.util.List;
import util.enumeration.CategoryEnum;
import util.enumeration.EndowmentProductEnum;
import util.enumeration.TermLifeProductEnum;
import util.enumeration.WholeLifeProductEnum;

/**
 *
 * @author sohqi
 */
public class ProductEntityWrapper {

    private ProductEntity product;
    private String productEnum;
    private String productType;
    private String isSmoker;
    
    // filter by category
//    private BigDecimal sumAssured;
//    private Integer coverageTerm;
//    private Integer premiumTerm;
//    private EndowmentProductEnum endowmentProductEnum;
//    private TermLifeProductEnum termLifeProductEnum;
//    private WholeLifeProductEnum wholeLifeProductEnum;
//    private CategoryEnum category;
//    private Boolean wantsRider;
//    private Boolean isSmoker;
    //adds on for add product
//    private List<RiderEntity> listOfider;
//    private List<FeatureEntity> listOfAdditionalFeature;
//    private List<PremiumEntity> listOfNonSmokerPremium;
//    private List<PremiumEntity> listOfSmokerPremium;
//    private ProductEntity productEntity;
    public ProductEntityWrapper() {
        this.product = new ProductEntity();
        
    }

    public ProductEntityWrapper(ProductEntity product, String productEnum, String isSmoker) {
        this();
        this.product = product;
        this.productEnum = productEnum;
        this.productType = productType;
        this.isSmoker = isSmoker;
    }

    public String getIsSmoker() {
        return isSmoker;
    }

    public void setIsSmoker(String isSmoker) {
        this.isSmoker = isSmoker;
    }

    
    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public String getProductEnum() {
        return productEnum;
    }

    public void setProductEnum(String productEnum) {
        this.productEnum = productEnum;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

}
