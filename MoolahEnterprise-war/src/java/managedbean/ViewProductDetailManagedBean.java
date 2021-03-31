/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.ProductEntity;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.helper.ProductEntityWrapper;

/**
 *
 * @author rayta
 */
@Named(value = "viewProductDetailManagedBean")
@ViewScoped
public class ViewProductDetailManagedBean implements Serializable {

    /**
     * Creates a new instance of ViewProductDetailManagedBean
     */
    private ProductEntityWrapper productToView;

    public ViewProductDetailManagedBean() {
    }

    
    @PostConstruct
    public void dataInit() {
        productToView = (ProductEntityWrapper) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productToView");
    }
    
    public ProductEntityWrapper getProductToView() {
        return productToView;
    }

    public void setProductToView(ProductEntityWrapper productToView) {
        this.productToView = productToView;
    }
}
