/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.CustomerEntity;
import ejb.entity.EndowmentEntity;
import ejb.entity.ProductEntity;
import ejb.entity.TermLifeProductEntity;
import ejb.entity.WholeLifeProductEntity;
import ejb.stateless.CustomerSessionBeanLocal;
import ejb.stateless.ProductSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.enumeration.EndowmentProductEnum;
import util.enumeration.TermLifeProductEnum;
import util.enumeration.WholeLifeProductEnum;
import util.exception.CustomerDoesNotExistsException;
import util.exception.CustomerUpdateException;
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;
import util.helper.ProductEntityWrapper;

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
            customerLikedProducts = customer.getListOfLikeProducts();
            filteredLikedProducts = customer.getListOfLikeProducts();
        } catch (CustomerDoesNotExistsException ex) {
            System.out.println("Customer does not exist");
        }
    }

    public ViewMyLikedProductsManagedBean() {

    }

    public void displayProductDetails(ActionEvent event) throws IOException {
        Long productIdToView = (Long) event.getComponent().getAttributes().get("product");
        try {
            ProductEntity productToView = productSessionBean.retrieveProductEntityById(productIdToView);
            ProductEntityWrapper wrapper = new ProductEntityWrapper(productToView, getParentClassAsString(productToView), getChildEnumAsString(productToView));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productToView", wrapper);
            FacesContext.getCurrentInstance().getExternalContext().redirect("../product/viewProductDetail.xhtml");
        } catch (ProductNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }

    }

    public void deleteFromLikedProducts(ActionEvent event) {
        try {
            ProductEntity productToRemove = (ProductEntity) event.getComponent().getAttributes().get("product");
            System.err.println("*********************** Product To Remove" + productToRemove.getProductId());
            customerLikedProducts.remove(productToRemove);
            filteredLikedProducts.remove(productToRemove);
            System.err.println("*********************** Product successfully removed from customer ");
            customer.getListOfLikeProducts().remove(productToRemove);
            System.err.println("*********************** Product successfully removed from customer BACKEND ");
            customerSessionBean.updateCustomer(customer);
            System.err.println("*********************** Updated customer successfully ");
        } catch (CustomerDoesNotExistsException | UnknownPersistenceException | CustomerUpdateException ex) {
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

    private String getParentClassAsString(ProductEntity p) {
        if (p instanceof EndowmentEntity) {
            return "Endowment";
        } else if (p instanceof TermLifeProductEntity) {
            return "Term Life";
        } else {
            return "Whole Life";
        }
    }

    private String getChildEnumAsString(ProductEntity p) {
        if (p instanceof EndowmentEntity) {
            EndowmentProductEnum tempEnum = ((EndowmentEntity) p).getProductEnum();
            if (tempEnum == EndowmentProductEnum.ENDOWMENT) {
                return "Endowment";
            }
        } else if (p instanceof TermLifeProductEntity) {
            TermLifeProductEnum tempEnum = ((TermLifeProductEntity) p).getProductEnum();
            if (tempEnum == TermLifeProductEnum.ACCIDENT) {
                return "Accident";
            } else if (tempEnum == TermLifeProductEnum.CRITICALILLNESS) {
                return "Critical Illness";
            } else {
                return "Hospital";
            }
        } else {
            WholeLifeProductEnum tempEnum = ((WholeLifeProductEntity) p).getProductEnum();
            if (tempEnum == WholeLifeProductEnum.ACCIDENT) {
                return "Accident";
            } else if (tempEnum == WholeLifeProductEnum.CRITICALILLNESS) {
                return "Critical Illness";
            } else if (tempEnum == WholeLifeProductEnum.LIFEINSURANCE) {
                return "Life Insurance";
            } else {
                return "Hospital";
            }
        }
        return "";
    }

}
