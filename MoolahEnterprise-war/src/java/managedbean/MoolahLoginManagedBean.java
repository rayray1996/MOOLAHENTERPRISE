/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.CompanyEntity;
import ejb.stateless.CompanySessionBeanLocal;
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
    private CompanySessionBeanLocal companySessionBeanLocal;
    private String username;
    private String password;
    private CompanyEntity companyEntity; 

    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    public void setCompanyEntity(CompanyEntity companyEntity) {
        this.companyEntity = companyEntity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    
      public void login(ActionEvent event) throws IOException
    {
        try
        {
            companyEntity = companySessionBeanLocal.login(username, password);
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("companyLogin", companyEntity);
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
        }
        catch(CompanyDoesNotExistException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Company does not exist: " + ex.getMessage(), null));
        }catch(IncorrectLoginParticularsException ex){
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));
        }
    }
    
    
    
    public void logout(ActionEvent event) throws IOException
    {
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
    }
}
