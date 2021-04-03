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
@Named(value = "endOfDUrationTotalSavingCalculatorManagedBean")
@ViewScoped
public class endOfDUrationTotalSavingCalculatorManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private BigDecimal currentlyHave;
    private BigDecimal inflationRate;
    private BigDecimal aimingAmt;
    private Integer noOfYear;
    private BigDecimal totalAmt;
    private AssetEntity assetEntity;
    private CustomerEntity tempCE;
    private BigDecimal currentlySaving;

    public BigDecimal getCurrentlySaving() {
        return currentlySaving;
    }

    public void setCurrentlySaving(BigDecimal currentlySaving) {
        this.currentlySaving = currentlySaving;
    }

    public static BigDecimal INFLATION_RATE = new BigDecimal("2.2");

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

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    @PostConstruct
    public void postConstruct() {
        tempCE = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
        assetEntity = tempCE.getAsset();
        currentlyHave = assetEntity.getCashInHand().add(assetEntity.getInvestments());
        inflationRate = INFLATION_RATE;
        currentlySaving = assetEntity.getMonthlyIncome().subtract(assetEntity.getMonthlyExpense());
    }

    /*
 * TBC again  
     */
    public void computeEndOfDurationTotalAmt(ActionEvent event) {

        BigDecimal intRate = BigDecimal.ZERO;
        
         //If user did not enter inflation rate, we will use 2.2%, otherwise we will use user's entered interest rate
        if (inflationRate.compareTo(BigDecimal.ZERO) == 0 || inflationRate == null) {
            intRate = INFLATION_RATE.divide(new BigDecimal("100"),7, RoundingMode.DOWN);
            inflationRate = INFLATION_RATE;
        } else {
            intRate = inflationRate.divide(new BigDecimal("100"),7, RoundingMode.DOWN);;
        }
        //get compound interest * principal Amt
        BigDecimal interestRate = intRate.add(new BigDecimal("1"));
        interestRate = new BigDecimal(Math.pow(interestRate.doubleValue(), noOfYear));
        BigDecimal tempCurrentlyHave = currentlyHave.multiply(interestRate);

        // difference
        BigDecimal onePlusR = BigDecimal.ONE.add(intRate);
        //calculate FVIFA
        BigDecimal negativePowerN = new BigDecimal(Math.pow(onePlusR.doubleValue(), noOfYear.doubleValue()));
        BigDecimal minusOne = negativePowerN.subtract(BigDecimal.ONE);
        totalAmt = (minusOne.divide(intRate, 7, RoundingMode.DOWN));
        totalAmt = totalAmt.multiply(currentlySaving);
        totalAmt = totalAmt.add(tempCurrentlyHave);
        //get final value
        totalAmt = totalAmt.divide(BigDecimal.ONE, 3, RoundingMode.DOWN);
    }

    /**
     * Creates a new instance of endOfDUrationTotalSavingCalculatorManagedBean
     */
    public endOfDUrationTotalSavingCalculatorManagedBean() {
    }

}
