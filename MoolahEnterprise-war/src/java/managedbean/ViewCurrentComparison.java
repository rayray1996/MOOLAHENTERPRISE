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
import java.util.Objects;
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
    private Integer comparisonSize;

    private ProductEntityWrapper firstProduct;
    private ProductEntityWrapper secondProduct;
    private ProductEntityWrapper thirdProduct;
    private ProductEntityWrapper fourthProduct;

    public ViewCurrentComparison() {
        currentComparisons = new ArrayList<>();
        comparisonSize = currentComparisons.size();
    }

    public void addToComparison(ProductEntityWrapper productToAdd) {
        if (currentComparisons.size() >= 4) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have reached the limit of 4 products to compare", null));
            return;
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
            if (Objects.equals(productToAdd.getProductEntity().getProductId(), p.getProductEntity().getProductId())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have already added this product", null));
                return;
            }
        }
        System.out.println("productToCompare = " + productToAdd.getStringChildCategory());
        currentComparisons.add(productToAdd);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have successfully added this to your compare list", null));
        comparisonSize = currentComparisons.size();
        productToAdd = null;
    }

    public void goToComparePage(ActionEvent event) {
//        FacesContext.getCurrentInstance().getExternalContext().redirect('');
    }

    public void deleteSelectedComparison(ActionEvent event) {
        ProductEntityWrapper toDeleteCompareProduct = (ProductEntityWrapper) event.getComponent().getAttributes().get("compareProductToDelete");
        if (toDeleteCompareProduct == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "An error has occurred, product is unable to delete", null));
            return;
        }

        currentComparisons.remove(toDeleteCompareProduct);
        comparisonSize = currentComparisons.size();
    }

    public List<ProductEntityWrapper> getCurrentComparisons() {
        return currentComparisons;
    }

    public void setCurrentComparisons(List<ProductEntityWrapper> currentComparisons) {
        this.currentComparisons = currentComparisons;
    }

    public Integer getComparisonSize() {
        return comparisonSize;
    }

    public void setComparisonSize(Integer comparisonSize) {
        this.comparisonSize = comparisonSize;
    }

    public ProductEntityWrapper getFirstProduct() {
        try {
            return currentComparisons.get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setFirstProduct(ProductEntityWrapper firstProduct) {
        this.firstProduct = firstProduct;
    }

    public ProductEntityWrapper getSecondProduct() {
        try {
            return currentComparisons.get(1);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setSecondProduct(ProductEntityWrapper secondProduct) {
        this.secondProduct = secondProduct;
    }

    public ProductEntityWrapper getThirdProduct() {
        try {
            return currentComparisons.get(2);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setThirdProduct(ProductEntityWrapper thirdProduct) {
        this.thirdProduct = thirdProduct;
    }

    public ProductEntityWrapper getFourthProduct() {
        try {
            return currentComparisons.get(3);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setFourthProduct(ProductEntityWrapper fourthProduct) {
        this.fourthProduct = fourthProduct;
    }

}
