/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.CompanyEntity;
import ejb.entity.CustomerEntity;
import ejb.stateless.CompanySessionBeanLocal;
import ejb.stateless.CustomerSessionBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import util.exception.CompanyDoesNotExistException;
import util.exception.IncorrectLoginParticularsException;

/**
 *
 * @author sohqi
 */
@Named(value = "moolahLoginManagedBean")
@RequestScoped
public class MoolahLoginManagedBean {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Creates a new instance of MoolahLoginManagedBean
     */
    public MoolahLoginManagedBean() {
    }

    public void login(ActionEvent event) throws IOException {
        try {
            CustomerEntity currentCustomer = customerSessionBean.login(email, password);
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("customerEntity", currentCustomer);
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
        } catch (IncorrectLoginParticularsException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));
        }
    }

    public void logout(ActionEvent event) throws IOException {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
    }
}
