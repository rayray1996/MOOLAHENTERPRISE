/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.AssetEntity;
import ejb.entity.CustomerEntity;
import ejb.stateless.CustomerSessionBeanLocal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author sohqi
 */
@Named(value = "durationToSaveCalculator")
@ViewScoped
public class DurationToSaveCalculatorManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    
    private BigDecimal currentlyHave;
    private BigDecimal inflationRate;
    private BigDecimal aimingAmt;
    private Integer noOfYear;
    private BigDecimal paymentAmt;
    private AssetEntity assetEntity;
    private CustomerEntity tempCE;
    private BigDecimal currentlySaving;

    public BigDecimal getCurrentlySaving() {
        return currentlySaving;
    }

    public void setCurrentlySaving(BigDecimal currentlySaving) {
        this.currentlySaving = currentlySaving;
    }
    
    /**
     * Creates a new instance of DurationToSaveCalculator
     */
    public DurationToSaveCalculatorManagedBean() {
    }
    
     public static BigDecimal INFLATION_RATE = new BigDecimal("0.022");

    public BigDecimal getCurrentlyHave() {
        return currentlyHave;
    }

    public void setCurrentlyHave(BigDecimal currentlyHave) {
        this.currentlyHave = currentlyHave;
    }

    public BigDecimal getInflationRate() {
        return inflationRate;
    }

    public void setInflationRate(BigDecimal inflationRate) {
        this.inflationRate = inflationRate;
    }

    public BigDecimal getAimingAmt() {
        return aimingAmt;
    }

    public void setAimingAmt(BigDecimal aimingAmt) {
        this.aimingAmt = aimingAmt;
    }

    public Integer getNoOfYear() {
        return noOfYear;
    }

    public void setNoOfYear(Integer noOfYear) {
        this.noOfYear = noOfYear;
    }

    public AssetEntity getAssetEntity() {
        return assetEntity;
    }

    public void setAssetEntity(AssetEntity assetEntity) {
        this.assetEntity = assetEntity;
    }

    public CustomerEntity getTempCE() {
        return tempCE;
    }

    public void setTempCE(CustomerEntity tempCE) {
        this.tempCE = tempCE;
    }

    public BigDecimal getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(BigDecimal paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    @PostConstruct
    public void postConstruct() {
        tempCE = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
       assetEntity = tempCE.getAsset();
       currentlyHave = assetEntity.getCashInHand().add(assetEntity.getInvestments());
       inflationRate = INFLATION_RATE;
       
    }
/*
 * TBC again  
 */
    public void computeMonthlySave(ActionEvent event) {
        currentlyHave = assetEntity.getCashInHand().add(assetEntity.getInvestments());
        BigDecimal noOfMonth = BigDecimal.ZERO;
        if (inflationRate.compareTo(BigDecimal.ZERO) == 0 || inflationRate == null) {
            noOfMonth = INFLATION_RATE;
            inflationRate = INFLATION_RATE;
        } else {
            noOfMonth = inflationRate;
        }
        BigDecimal interestRate = noOfMonth.add(new BigDecimal("1"));
        interestRate = new BigDecimal(Math.pow(interestRate.doubleValue(),  noOfYear));
        BigDecimal tempCurrentlyHave = currentlyHave.multiply(interestRate);
        System.out.println("computed monthly :" + currentlyHave.toString());

        // difference
        BigDecimal difference = aimingAmt.subtract(tempCurrentlyHave);
        //BigDecimal onePlusR = BigDecimal.ONE.add(inflationRate);
        //BigDecimal negativePowerN = new BigDecimal(Math.pow(onePlusR.doubleValue(), (-1) * noOfYear.doubleValue())) ;
        //BigDecimal minusOne = negativePowerN.subtract(BigDecimal.ONE);
        //BigDecimal fvifa = minusOne.divide(inflationRate);
        BigDecimal onePlusR = BigDecimal.ONE.add(inflationRate);
        BigDecimal negativePowerN = new BigDecimal(Math.pow(onePlusR.doubleValue(), (-1) * noOfYear.doubleValue()));
        BigDecimal oneMinus = BigDecimal.ONE.subtract(negativePowerN);
        BigDecimal pvifa = oneMinus.divide(inflationRate, 6, RoundingMode.HALF_UP);
        paymentAmt = difference.divide(pvifa, 3, RoundingMode.HALF_UP);

    }
}
