/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.CompanyEntity;
import ejb.entity.ProductEntity;
import ejb.stateless.CompanySessionBeanLocal;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.helper.ProductEntityWrapper;

/**
 *
 * @author nickg
 */
@Named(value = "interestRateManagedBean")
@RequestScoped
public class InterestRateManagedBean {

    @EJB
    private CompanySessionBeanLocal companySessionBean;

    private String companyUrl;

    private ProductEntityWrapper product;
    
    public InterestRateManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        setProduct((ProductEntityWrapper) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productToView"));
        companyUrl = getProduct().getProductEntity().getCompany().getCompanyUrl();
    }

    public void redirectMe(ActionEvent event) throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(companyUrl);
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public ProductEntityWrapper getProduct() {
        return product;
    }

    public void setProduct(ProductEntityWrapper product) {
        this.product = product;
    }


}
