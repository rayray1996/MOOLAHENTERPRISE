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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
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
@SessionScoped
public class KeyPasswordManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private CustomerEntity customer;
    
    private String newPassword;

    public KeyPasswordManagedBean() {
    }

    @PostConstruct
    public void init() {
        try {
            String customerId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("customerId");
            setCustomer(customerSessionBean.retrieveCustomerById(Long.parseLong(customerId))); 
            
        } catch (CustomerDoesNotExistsException ex) {
            Logger.getLogger(KeyPasswordManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateCustomerPassword(ActionEvent event) {

        try {

            getCustomer().setPassword(getNewPassword());
            customerSessionBean.updateCustomer(getCustomer());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have successfully updated your password!", null));
        } catch (CustomerDoesNotExistsException | UnknownPersistenceException | CustomerUpdateException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


}
