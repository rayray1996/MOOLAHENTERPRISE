/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.ComparisonEntity;
import ejb.entity.ProductEntity;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.helper.ProductEntityWrapper;

/**
 *
 * @author rayta
 */
@Named(value = "viewSavedComparisonDetailManagedBean")
@ViewScoped
public class ViewSavedComparisonDetailManagedBean implements Serializable {

    private ComparisonEntity comparisonToView;
    private String dateAdded;

    private List<ProductEntity> products;

    private ProductEntity firstProduct;
    private ProductEntity secondProduct;
    private ProductEntity thirdProduct;
    private ProductEntity fourthProduct;

    public ViewSavedComparisonDetailManagedBean() {
        products = new ArrayList();
    }

    @PostConstruct
    public void dataInit() {
        comparisonToView = (ComparisonEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comparisonToView");
        if (comparisonToView == null) {
            products = new ArrayList<>();
            return;
        }
        products = comparisonToView.getProductsToCompare();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateRequested = format.format(Calendar.getInstance().getTime());
        dateAdded = dateRequested;
    }

    public ProductEntity getFirstProduct() {
        try {
            return products.get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setFirstProduct(ProductEntity firstProduct) {
        this.firstProduct = firstProduct;
    }

    public ProductEntity getSecondProduct() {
        try {
            return products.get(1);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setSecondProduct(ProductEntity secondProduct) {
        this.secondProduct = secondProduct;
    }

    public ProductEntity getThirdProduct() {
        try {
            return products.get(2);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setThirdProduct(ProductEntity thirdProduct) {
        this.thirdProduct = thirdProduct;
    }

    public ProductEntity getFourthProduct() {
        try {
            return products.get(3);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setFourthProduct(ProductEntity fourthProduct) {
        this.fourthProduct = fourthProduct;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    public ComparisonEntity getComparisonToView() {
        return comparisonToView;
    }

    public void setComparisonToView(ComparisonEntity comparisonToView) {
        this.comparisonToView = comparisonToView;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
