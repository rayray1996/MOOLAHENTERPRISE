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
public class ProductWrapper {

    // filter by category
    private BigDecimal sumAssured;
    private Integer coverageTerm;
    private Integer premiumTerm;
    private EndowmentProductEnum endowmentProductEnum;
    private TermLifeProductEnum termLifeProductEnum;
    private WholeLifeProductEnum wholeLifeProductEnum;
    private CategoryEnum category;
    private Boolean wantsRider;
    private Boolean isSmoker;

    //adds on for add product
    private List<RiderEntity> listOfider;
    private List<FeatureEntity> listOfAdditionalFeature;
    private List<PremiumEntity> listOfNonSmokerPremium;
    private List<PremiumEntity> listOfSmokerPremium;
    private ProductEntity productEntity;

    public List<RiderEntity> getListOfider() {
        return listOfider;
    }

    public void setListOfider(List<RiderEntity> listOfider) {
        this.listOfider = listOfider;
    }

    public List<FeatureEntity> getListOfAdditionalFeature() {
        return listOfAdditionalFeature;
    }

    public void setListOfAdditionalFeature(List<FeatureEntity> listOfAdditionalFeature) {
        this.listOfAdditionalFeature = listOfAdditionalFeature;
    }

    public List<PremiumEntity> getListOfNonSmokerPremium() {
        return listOfNonSmokerPremium;
    }

    public void setListOfNonSmokerPremium(List<PremiumEntity> listOfNonSmokerPremium) {
        this.listOfNonSmokerPremium = listOfNonSmokerPremium;
    }

    public List<PremiumEntity> getListOfSmokerPremium() {
        return listOfSmokerPremium;
    }

    public void setListOfSmokerPremium(List<PremiumEntity> listOfSmokerPremium) {
        this.listOfSmokerPremium = listOfSmokerPremium;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Boolean getWantsRider() {
        return wantsRider;
    }

    public void setWantsRider(Boolean wantsRider) {
        this.wantsRider = wantsRider;
    }

    public Boolean getIsSmoker() {
        return isSmoker;
    }

    public void setIsSmoker(Boolean isSmoker) {
        this.isSmoker = isSmoker;
    }

    public BigDecimal getSumAssured() {
        return sumAssured;
    }

    public void setSumAssured(BigDecimal sumAssured) {
        this.sumAssured = sumAssured;
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

    public EndowmentProductEnum getEndowmentProductEnum() {
        return endowmentProductEnum;
    }

    public void setEndowmentProductEnum(EndowmentProductEnum endowmentProductEnum) {
        this.endowmentProductEnum = endowmentProductEnum;
    }

    public TermLifeProductEnum getTermLifeProductEnum() {
        return termLifeProductEnum;
    }

    public void setTermLifeProductEnum(TermLifeProductEnum termLifeProductEnum) {
        this.termLifeProductEnum = termLifeProductEnum;
    }

    public WholeLifeProductEnum getWholeLifeProductEnum() {
        return wholeLifeProductEnum;
    }

    public void setWholeLifeProductEnum(WholeLifeProductEnum wholeLifeProductEnum) {
        this.wholeLifeProductEnum = wholeLifeProductEnum;
    }

}
