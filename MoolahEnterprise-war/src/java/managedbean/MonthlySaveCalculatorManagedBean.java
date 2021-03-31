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
import util.exception.AssetEntityDoesNotExistException;

/**
 *
 * @author sohqi
 */
@Named(value = "monthlySaveCalculatorManagedBean")
@ViewScoped
public class MonthlySaveCalculatorManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private BigDecimal currentlyHave;
    private BigDecimal inflationRate;
    private BigDecimal aimingAmt;
    private Integer noOfYear;
    private BigDecimal paymentAmt;
    private AssetEntity assetEntity;
    private CustomerEntity tempCE;

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

    public BigDecimal getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(BigDecimal paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    /**
     * Creates a new instance of MonthlySaveCalculatorManagedBean
     */
    public MonthlySaveCalculatorManagedBean() {

    }

    @PostConstruct
    public void postConstruct() {
        tempCE = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
        
       assetEntity = tempCE.getAsset() ;
       currentlyHave = assetEntity.getCashInHand().add(assetEntity.getInvestments());
       inflationRate = INFLATION_RATE;
       
    }

    public void computeMonthlySave(ActionEvent event) {
        BigDecimal intRateInPercentage = BigDecimal.ZERO;
        //If user did not enter inflation rate, we will use 2.2%, otherwise we will use user's entered interest rate
        if (inflationRate.compareTo(BigDecimal.ZERO) == 0 || inflationRate == null) {
            intRateInPercentage = INFLATION_RATE.divide(new BigDecimal("100"),7, RoundingMode.DOWN);
            inflationRate = INFLATION_RATE.divide(new BigDecimal("100"),7, RoundingMode.DOWN);
        } else {
            intRateInPercentage = inflationRate.divide(new BigDecimal("100"),7, RoundingMode.DOWN);
        }
        //Get compound interest * principal 
        BigDecimal interestRate = intRateInPercentage.add(new BigDecimal("1"));
        interestRate = new BigDecimal(Math.pow(interestRate.doubleValue(),  noOfYear));
        BigDecimal tempCurrentlyHave = currentlyHave.multiply(interestRate);

        // difference in sum at last year, see how much it lacks
        BigDecimal difference = aimingAmt.subtract(tempCurrentlyHave);
        //compute PVIFA formula
        BigDecimal onePlusR = BigDecimal.ONE.add(intRateInPercentage);
        BigDecimal negativePowerN = new BigDecimal(Math.pow(onePlusR.doubleValue(), (-1) * noOfYear.doubleValue()));
        BigDecimal oneMinus = BigDecimal.ONE.subtract(negativePowerN);
        BigDecimal pvifa = oneMinus.divide(intRateInPercentage, 6, RoundingMode.DOWN);
        //get final amount
        paymentAmt = difference.divide(pvifa, 3, RoundingMode.DOWN);
    }

}
