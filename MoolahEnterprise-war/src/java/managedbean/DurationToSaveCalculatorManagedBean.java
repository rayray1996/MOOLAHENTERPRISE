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
import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;
import static managedbean.endOfDUrationTotalSavingCalculatorManagedBean.INFLATION_RATE;

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
    public void computeHowLong(ActionEvent event) {
        int tempMonth = 1;
        boolean hasReachedTheTarget = true;
        BigDecimal intRate = BigDecimal.ZERO;
        //If user did not enter inflation rate, we will use 2.2%, otherwise we will use user's entered interest rate
        if (inflationRate.compareTo(BigDecimal.ZERO) == 0 || inflationRate == null) {
            intRate = INFLATION_RATE.divide(new BigDecimal("100"), 7, RoundingMode.DOWN);
        } else {
            intRate = inflationRate.divide(new BigDecimal("100"), 7, RoundingMode.DOWN);;
        }
        //get compound interest * principal Amt
        System.out.println("intRate :" + intRate);

        BigDecimal interestRate = intRate.add(new BigDecimal("1"));
        double actualYear = 0;
        while (hasReachedTheTarget) {

            if (tempMonth > 11) {
                actualYear = actualYear + 1;
                tempMonth = 0;
            }
            if (tempMonth >= 1) {
                actualYear = actualYear + (((double) tempMonth) / 12.0);
                System.out.println("************tempMonth/12.0***************" + (((double) tempMonth) / 12.0));
            }
            System.out.println("******************actualYear*********" + actualYear);
            System.out.println("******** tempMonth :" + tempMonth + " tempMonth" + actualYear + " *********");
            BigDecimal tempTargetValue = BigDecimal.ZERO;

            BigDecimal tempInterestRate = new BigDecimal(Math.pow(interestRate.doubleValue(), actualYear));
            BigDecimal tempCurrentlyHave = currentlyHave.multiply(tempInterestRate);
            System.out.println("tempCurrentlyHave:" + tempCurrentlyHave);
            tempTargetValue = tempTargetValue.add(tempCurrentlyHave);
            System.out.println("tempTargetValue:" + tempTargetValue);
            // difference
            System.out.println("aimingAmt:" + aimingAmt);
            BigDecimal onePlusR = BigDecimal.ONE.add(intRate);
            System.out.println("onePlusR:" + onePlusR);
            BigDecimal negativePowerN = new BigDecimal(Math.pow(onePlusR.doubleValue(), (actualYear)));
            System.out.println("negativePowerN:" + negativePowerN);
            BigDecimal minusOne = negativePowerN.subtract(BigDecimal.ONE);
            System.out.println("minusOne:" + minusOne);
            BigDecimal fvifa = minusOne.divide(intRate, 7, RoundingMode.DOWN);
            System.out.println("fvifa:" + fvifa);
            BigDecimal tempSaving = fvifa.multiply(currentlySaving);
            System.out.println("tempSaving:" + tempSaving);
            tempTargetValue = tempTargetValue.add(tempSaving);
            System.out.println("tempTargetValue:" + tempTargetValue);
            if (aimingAmt.compareTo(tempTargetValue) <= 0) {

                hasReachedTheTarget = false;
                break;
            }
            tempMonth += 1;

        }

    }
}
