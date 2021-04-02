/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.ComparisonEntity;
import ejb.entity.CustomerEntity;
import ejb.entity.ProductEntity;
import ejb.stateless.ComparisonSessionBeanLocal;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.ComparisonErrorException;
import util.exception.UnknownPersistenceException;
import util.helper.ProductEntityWrapper;

/**
 *
 * @author rayta
 */
@Named(value = "viewCurrentComparison")
@SessionScoped
public class ViewCurrentComparison implements Serializable {

    @EJB
    private ComparisonSessionBeanLocal comparisonSessionBean;

//    private ProductEntityWrapper productToAdd;
    private List<ProductEntityWrapper> currentComparisons;
    private Integer comparisonSize;

    private ProductEntityWrapper firstProduct;
    private ProductEntityWrapper secondProduct;
    private ProductEntityWrapper thirdProduct;
    private ProductEntityWrapper fourthProduct;

    private String currentDate;
    private ComparisonEntity comparisonEntity;

    public ViewCurrentComparison() {
        currentComparisons = new ArrayList<>();
        comparisonSize = currentComparisons.size();
        comparisonEntity = new ComparisonEntity();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateRequested = format.format(Calendar.getInstance().getTime());
        currentDate = dateRequested;
    }

    public void addToComparison(ProductEntityWrapper productToAdd) {
        if (currentComparisons.size() >= 4) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have reached the limit of 4 products to compare", null));
            return;
        }
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
        currentComparisons.add(productToAdd);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have successfully added this to your compare list", null));
        comparisonSize = currentComparisons.size();
        productToAdd = null;
    }

    public void goToComparePage(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("compareProductsPage.xhtml");
    }

    public void deleteSelectedComparison(ActionEvent event) {
        System.out.println("in here");
        ProductEntityWrapper toDeleteCompareProduct = (ProductEntityWrapper) event.getComponent().getAttributes().get("compareProductToDelete");
        if (toDeleteCompareProduct == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "An error has occurred, product is unable to delete", null));
            return;
        }

        currentComparisons.remove(toDeleteCompareProduct);
        comparisonSize = currentComparisons.size();
    }

    
    public void saveComparison(ActionEvent event) {
        try {
            System.out.println("here");
            CustomerEntity customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
            List<ProductEntity> listOfProducts = new ArrayList<>();
            for (ProductEntityWrapper p : currentComparisons) {
                listOfProducts.add(p.getProductEntity());
            }

            comparisonEntity.setDateOfCompletion(Calendar.getInstance());
            comparisonEntity.setProductsToCompare(listOfProducts);
            comparisonSessionBean.saveThisComparison(comparisonEntity);
            customer.getSavedComparisons().add(comparisonEntity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comparison " + comparisonEntity.getComparisonName() + " has been saved successfully", null));
            comparisonEntity = new ComparisonEntity();
            currentComparisons = new ArrayList<>();
            comparisonSize = 0;
        } catch (UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Something went wrong. Please try again", null));
        } catch (ComparisonErrorException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "This comparison is not unique", null));
        }
    }

    public void redirectToViewRecommendedProduct() throws IOException {
        System.out.println("redircetR");
        FacesContext.getCurrentInstance().getExternalContext().redirect("ViewRecommendedProduct.xhtml");
    }

    public void redirectToViewAllProduct() throws IOException {
        System.out.println("redirectA");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllProduct.xhtml");
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

    public ComparisonEntity getComparisonEntity() {
        return comparisonEntity;
    }

    public void setComparisonEntity(ComparisonEntity comparisonEntity) {
        this.comparisonEntity = comparisonEntity;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

}
