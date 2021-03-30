/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.CustomerEntity;
import ejb.stateless.CustomerSessionBeanLocal;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CustomerDoesNotExistsException;
import util.exception.CustomerUpdateException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Ada Wong
 */
@Named(value = "keyPasswordManagedBean")
@ViewScoped
public class KeyPasswordManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private String newPassword;

    public KeyPasswordManagedBean() {
    }

    public void updateCustomerPassword(ActionEvent event) throws CustomerUpdateException {

        CustomerEntity customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("changePwCust");
        customer.setPassword(getNewPassword());
        try {
            customerSessionBean.updateCustomer(customer);
        } catch (CustomerDoesNotExistsException | UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        } 
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    
}
