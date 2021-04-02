/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helper;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author nickg
 */
public class AffordabilityWrapper implements Serializable{
    private BigDecimal affordability;
    private BigDecimal amountAfterPremium;

    public AffordabilityWrapper() {
    }

    public AffordabilityWrapper(BigDecimal affordability, BigDecimal amountAfterPremium) {
        this.affordability = affordability;
        this.amountAfterPremium = amountAfterPremium;
    }

    public BigDecimal getAffordability() {
        return affordability;
    }

    public void setAffordability(BigDecimal affordability) {
        this.affordability = affordability;
    }

    public BigDecimal getAmountAfterPremium() {
        return amountAfterPremium;
    }

    public void setAmountAfterPremium(BigDecimal amountAfterPremium) {
        this.amountAfterPremium = amountAfterPremium;
    }
    
    
    
}
