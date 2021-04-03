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
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;

@Named(value = "viewMyLikedProductsManagedBean")
@ViewScoped
public class ViewMyLikedProductsManagedBean implements Serializable {

    @EJB
    private ProductSessionBeanLocal productSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    
    private CustomerEntity customer;

    private List<ProductEntity> customerLikedProducts;
    
    private List<ProductEntity> filteredLikedProducts;

    @PostConstruct
    public void init() {
        setCustomer((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity"));
        Long custId = getCustomer().getCustomerId();
        try {
            customer = customerSessionBean.retrieveCustomerById(custId);
        } catch (CustomerDoesNotExistsException ex) {
            System.out.println("Customer does not exist");
        }
        customerLikedProducts = customer.getListOfLikeProducts();
    }
    public ViewMyLikedProductsManagedBean() {
        filteredLikedProducts = new ArrayList<>();
        

    }
   
    
    public void displayProductDetails(ActionEvent event) throws IOException{
        Long productIdToView = (Long)event.getComponent().getAttributes().get("product");
        try {
            ProductEntity productToView = productSessionBean.retrieveProductEntityById(productIdToView);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productToView", productToView);
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
        } catch (ProductNotFoundException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
        
        
    }
    
    public void deleteFromLikedProducts (ActionEvent event) {
        try {
            ProductEntity productToRemove = (ProductEntity)event.getComponent().getAttributes().get("product");
            customerLikedProducts.remove(productToRemove);
            customer.getListOfLikeProducts().remove(productToRemove);
            customerSessionBean.updateCustomer(customer);
        } catch (CustomerDoesNotExistsException |UnknownPersistenceException| CustomerUpdateException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Product cannot be deleted!", null));
        }
        
    }
    

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public List<ProductEntity> getCustomerLikedProducts() {
        return customerLikedProducts;
    }

    public void setCustomerLikedProducts(List<ProductEntity> customerLikedProducts) {
        this.customerLikedProducts = customerLikedProducts;
    }

    public List<ProductEntity> getFilteredLikedProducts() {
        return filteredLikedProducts;
    }

    public void setFilteredLikedProducts(List<ProductEntity> filteredLikedProducts) {
        this.filteredLikedProducts = filteredLikedProducts;
    }
    
    

}
