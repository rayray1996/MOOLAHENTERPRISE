/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.ComparisonEntity;
import ejb.entity.CustomerEntity;
import ejb.entity.PremiumEntity;
import ejb.entity.ProductEntity;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

    private List<ProductEntityWrapper> products;

    private ProductEntityWrapper firstProduct;
    private ProductEntityWrapper secondProduct;
    private ProductEntityWrapper thirdProduct;
    private ProductEntityWrapper fourthProduct;

    public ViewSavedComparisonDetailManagedBean() {
        products = new ArrayList();
    }

    @PostConstruct
    public void dataInit() {
        comparisonToView = (ComparisonEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comparisonToView");
        CustomerEntity currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
        if (comparisonToView == null || currentCustomer == null) {
            products = new ArrayList<>();
            return;
        }
        for (ProductEntity prod : comparisonToView.getProductsToCompare()) {
            ProductEntityWrapper temp = new ProductEntityWrapper(prod, null, null);
            int age = new GregorianCalendar().get(GregorianCalendar.YEAR) - currentCustomer.getDateOfBirth().get(GregorianCalendar.YEAR);
            // add the correct premium to wrapper
            for (PremiumEntity p : prod.getListOfPremium()) {
                if (age >= p.getMinAgeGroup() && age <= p.getMaxAgeGroup()) {
                    temp.setNormalPremium(p);
                    break;
                }
            }

            // add the correct smoker premium to wrapper
            for (PremiumEntity p : prod.getListOfSmokerPremium()) {
                if (age >= p.getMinAgeGroup() && age <= p.getMaxAgeGroup()) {
                    temp.setSmokerPremium(p);
                    break;
                }
            }

            products.add(temp);

        }

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateRequested = format.format(Calendar.getInstance().getTime());
        dateAdded = dateRequested;
    }

    public ProductEntityWrapper getFirstProduct() {
        try {
            return products.get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setFirstProduct(ProductEntityWrapper firstProduct) {
        this.firstProduct = firstProduct;
    }

    public ProductEntityWrapper getSecondProduct() {
        try {
            return products.get(1);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setSecondProduct(ProductEntityWrapper secondProduct) {
        this.secondProduct = secondProduct;
    }

    public ProductEntityWrapper getThirdProduct() {
        try {
            return products.get(2);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setThirdProduct(ProductEntityWrapper thirdProduct) {
        this.thirdProduct = thirdProduct;
    }

    public ProductEntityWrapper getFourthProduct() {
        try {
            return products.get(3);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setFourthProduct(ProductEntityWrapper fourthProduct) {
        this.fourthProduct = fourthProduct;
    }

    public List<ProductEntityWrapper> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntityWrapper> products) {
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
