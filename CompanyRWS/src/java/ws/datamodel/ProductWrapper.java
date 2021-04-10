/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import java.math.BigDecimal;
import util.enumeration.CategoryEnum;
import util.enumeration.EndowmentProductEnum;
import util.enumeration.TermLifeProductEnum;
import util.enumeration.WholeLifeProductEnum;

/**
 *
 * @author sohqi
 */
public class ProductWrapper {

    private CategoryEnum category;
    private Boolean wantsRider;
    private Boolean isSmoker;
    private BigDecimal sumAssured;
    private Integer coverageTerm;
    private Integer premiumTerm;
    private EndowmentProductEnum endowmentProductEnum;
    private TermLifeProductEnum termLifeProductEnum;
    private WholeLifeProductEnum wholeLifeProductEnum;

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
