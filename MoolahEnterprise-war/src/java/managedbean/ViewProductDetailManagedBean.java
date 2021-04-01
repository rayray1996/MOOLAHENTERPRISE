/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.ProductEntity;
import ejb.stateless.ProductSessionBeanLocal;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.ProductNotFoundException;
import util.helper.ProductEntityWrapper;

/**
 *
 * @author rayta
 */
@Named(value = "viewProductDetailManagedBean")
@ViewScoped
public class ViewProductDetailManagedBean implements Serializable {

    @EJB
    private ProductSessionBeanLocal productSessionBean;


    private ProductEntityWrapper productToView;
    
    private ProductEntity product;

    
    
    public ViewProductDetailManagedBean() {
    }

    
    @PostConstruct
    public void dataInit() {
        try {
            productToView = (ProductEntityWrapper) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productToView");
            setProduct(productSessionBean.retrieveProductEntityById(productToView.getProductEntity().getProductId()));
        } catch (ProductNotFoundException ex) {
            System.out.println("Product does not exists!");
        }
    }
    
    public ProductEntityWrapper getProductToView() {
        return productToView;
    }

    public void setProductToView(ProductEntityWrapper productToView) {
        this.productToView = productToView;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }
}
