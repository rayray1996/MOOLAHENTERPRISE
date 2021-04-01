/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.CustomerEntity;
import ejb.entity.ProductEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.helper.ProductEntityWrapper;

/**
 *
 * @author rayta
 */
@Named(value = "viewCurrentComparison")
@SessionScoped
public class ViewCurrentComparison implements Serializable {

//    private ProductEntityWrapper productToAdd;

    private List<ProductEntityWrapper> currentComparisons;

    public ViewCurrentComparison() {
        currentComparisons = new ArrayList<>();
    }

    public void addToComparison(ProductEntityWrapper productToAdd) {
        if(currentComparisons.size() >= 4) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have reached the limit of 4 products to compare", null));
        }
        System.out.println("productToAdd = " + productToAdd.toString());
        CustomerEntity currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");

        if (currentCustomer == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please log in to compare products", null));
            return;
        }

        // check if productToCompare is in the same childCategory as the other current compare products
        for (ProductEntityWrapper p : currentComparisons) {
            if (!(p.getStringChildCategory().equals(productToAdd.getStringChildCategory()))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You can only compare products of the same type! \n If you wish to compare other products, please clear your current comparisons.", null));
                return;
            }
        }
        System.out.println("productToCompare = " + productToAdd.getStringChildCategory());
        currentComparisons.add(productToAdd);
        productToAdd = null;
    }

    public List<ProductEntityWrapper> getCurrentComparisons() {
        return currentComparisons;
    }

    public void setCurrentComparisons(List<ProductEntityWrapper> currentComparisons) {
        this.currentComparisons = currentComparisons;
    }

}
