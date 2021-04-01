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
@Named(value = "myProfileManagedBean")
@ViewScoped
public class MyProfileManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private CustomerEntity customer;

    @PostConstruct
    public void init() {
        customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
    }

    public MyProfileManagedBean() {
    }

    public void updateMyProfile(ActionEvent event) {
        try {
            customerSessionBean.updateCustomer(getCustomer());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have successfully updated your profile", null));
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

}
