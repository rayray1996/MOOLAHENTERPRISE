/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author sohqi
 */
@Named(value = "financialCalculator_HomeManagedBean")
@ViewScoped
public class FinancialCalculator_HomeManagedBean implements Serializable {

    /**
     * Creates a new instance of FinancialCalculator_HomeManagedBean
     */
    public FinancialCalculator_HomeManagedBean() {
    }
    public void monthlySaveToAchieveGoals(ActionEvent actionEvent)throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("monthlySaveToAchieveCalculator.xhtml");
    }
    public void howLongToSaveAmt(ActionEvent actionEvent)throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("durationToSaveCalculator.xhtml");
    }
    public void yearlyAmtAfterSaving(ActionEvent actionEvent)throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("endOfDurationTotalSavingCalculator.xhtml");
    }
    
}
