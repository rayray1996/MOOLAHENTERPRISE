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
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.CustomerDoesNotExistsException;
import util.exception.CustomerPasswordExistsException;

/**
 *
 * @author Ada Wong
 */
@Named(value = "resetPasswordManagedBean")
@ViewScoped
public class ResetPasswordManagedBean implements Serializable{

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    
    private String email; 
    
    public ResetPasswordManagedBean() {
    }
    
    
    public void resetCustomerPassword(ActionEvent event){
        try {
            customerSessionBean.resetPassword(getEmail());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "A link has successfully been sent to your email for you to reset your password", null));
        } catch (CustomerPasswordExistsException | CustomerDoesNotExistsException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

            
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
    
}
