/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.CustomerEntity;
import ejb.stateless.CustomerSessionBeanLocal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CustomerAlreadyExistException;
import util.exception.CustomerCreationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rayta
 */
@Named(value = "createAccountManagedBean")
@RequestScoped
public class CreateAccountManagedBean {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private CustomerEntity customer;
    private Date dateOfBirth;
    private Date currentDate;

    public Date getCurrentDate() {
        return currentDate;
    }

    public CreateAccountManagedBean() {
        customer = new CustomerEntity();
        currentDate = new Date();
    }

    public void createAccount(ActionEvent event) {
        try {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(dateOfBirth);
            customer.setDateOfBirth(gc);
            CustomerEntity newCustomer = customerSessionBean.createCustomer(customer);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account successfully created! (ID: " + customer.getCustomerId() + ")", null));
            customer = new CustomerEntity();
            dateOfBirth = null;
        } catch (CustomerAlreadyExistException | UnknownPersistenceException | CustomerCreationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, ex.toString(), null));
        }
        
        
    }
    
   
    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}
