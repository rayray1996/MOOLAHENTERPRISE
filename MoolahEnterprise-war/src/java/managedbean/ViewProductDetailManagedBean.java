/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.CustomerEntity;
import ejb.entity.ProductEntity;
import ejb.stateless.CustomerSessionBeanLocal;
import ejb.stateless.ProductSessionBeanLocal;
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
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;
import util.helper.ProductEntityWrapper;

/**
 *
 * @author rayta
 */
@Named(value = "viewProductDetailManagedBean")
@ViewScoped
public class ViewProductDetailManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private ProductSessionBeanLocal productSessionBean;

    private ProductEntityWrapper productToView;

    private ProductEntity product;

    private Boolean customerLiked;

    public ViewProductDetailManagedBean() {
    }

    @PostConstruct
    public void dataInit() {
        try {
            productToView = (ProductEntityWrapper) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productToView");
            setProduct(productSessionBean.retrieveProductEntityById(productToView.getProductEntity().getProductId()));
            customerLikeProduct();
        } catch (ProductNotFoundException ex) {
            System.out.println("Product does not exists!");
        }
    }

    public void likeProduct(ActionEvent event) {
        try {
            CustomerEntity cust = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
            if (cust.getListOfLikeProducts().contains(product)) {
                cust = customerSessionBean.retrieveCustomerById(cust.getCustomerId());
                cust.getListOfLikeProducts().remove(product);
                customerSessionBean.updateCustomer(cust);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product has been removed from your profile!", null));
                customerLikeProduct();
            } else {
                customerSessionBean.likeAProduct(cust.getCustomerId(), product.getProductId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product has been saved to your profile!", null));
                customerLikeProduct();
            }

        } catch (CustomerDoesNotExistsException | ProductNotFoundException | UnknownPersistenceException | CustomerUpdateException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
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

    public void customerLikeProduct() {
        try {
            CustomerEntity cust = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
            cust = customerSessionBean.retrieveCustomerById(cust.getCustomerId());
            if (cust.getListOfLikeProducts().contains(product)) {
                customerLiked = true;
            } else {
                customerLiked = false;
            }
        } catch (CustomerDoesNotExistsException ex) {
            Logger.getLogger(ViewProductDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean getCustomerLiked() {
        return customerLiked;
    }

    public void setCustomerLiked(Boolean customerLiked) {
        this.customerLiked = customerLiked;
    }
}
