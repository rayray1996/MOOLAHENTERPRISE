/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.CustomerEntity;
import ejb.stateless.CustomerSessionBeanLocal;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    
    private Date dob;
    
    private String dateOfBirth;
    
    private Boolean editable;
    
    private String changePassword;
    
    @PostConstruct
    public void init() {
        customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
        setDob(customer.getDateOfBirth().getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY");
        dateOfBirth = format.format(dob);
        editable = false;
    }

    public MyProfileManagedBean() {
    }

    public void updateMyProfile(ActionEvent event) {
        try {
            
            
            customerSessionBean.updateCustomer(customer);
            setEditable(false);
           
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have successfully updated your profile", null));
        } catch (CustomerDoesNotExistsException | UnknownPersistenceException | CustomerUpdateException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }  
    
    public void updateForm(ActionEvent event){
        setEditable(true);
    }
    
    public void updateCustomerPassword(ActionEvent event){
        customer.setPassword(getChangePassword());       
    }
   

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(String changePassword) {
        this.changePassword = changePassword;
    }

}
