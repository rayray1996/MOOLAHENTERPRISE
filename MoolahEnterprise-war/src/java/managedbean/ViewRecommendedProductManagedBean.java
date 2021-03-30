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
import java.math.BigDecimal;
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
import javax.inject.Inject;
import util.enumeration.CategoryEnum;
import util.enumeration.EndowmentProductEnum;
import util.enumeration.TermLifeProductEnum;
import util.enumeration.WholeLifeProductEnum;
import util.exception.CustomerDoesNotExistsException;
import util.exception.ProductNotFoundException;
import util.helper.ProductEntityWrapper;

/**
 *
 * @author rayta
 */
@Named(value = "viewRecommendedProductManagedBean")
@ViewScoped
public class ViewRecommendedProductManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private ProductSessionBeanLocal productSessionBean;
    
    @Inject
    private ViewProductDetailManagedBean viewProductDetailManagedBean;
    
    private CustomerEntity customer;
    private ProductEntityWrapper productToView;

    private List<ProductEntityWrapper> listOfProducts;
    private List<ProductEntityWrapper> filteredProducts;
    private List<ProductEntityWrapper> searchedProducts;

    private List<CategoryEnum> categories;
    private CategoryEnum filterCategory;
    private Integer filterCoverageTerm;
    private Integer filterPremiumTerm;
    private BigDecimal filterSumAssured;

    public ViewRecommendedProductManagedBean() {
        listOfProducts = new ArrayList<>();
        filteredProducts = new ArrayList<>();
        searchedProducts = new ArrayList<>();
        categories = new ArrayList<>();
    }

    @PostConstruct
    public void dataInit() {
        customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
//        try {
//            List<ProductEntity> tempProducts = customerSessionBean.retrieveRecommendedProducts(customer.getCustomerId());
            List<ProductEntity> tempProducts = productSessionBean.retrieveAllFinancialProducts();
            for (ProductEntity p : tempProducts) {
                listOfProducts.add(new ProductEntityWrapper(p, getParentClassAsString(p), getChildEnumAsString(p)));
            }

//        } catch (CustomerDoesNotExistsException ex) {
//            FacesContext.getCurrentInstance().addMessage("null", new FacesMessage(FacesMessage.SEVERITY_INFO, "You are not logged in!", ""));
//        } catch (ProductNotFoundException ex) {
//            FacesContext.getCurrentInstance().addMessage("null", new FacesMessage(FacesMessage.SEVERITY_INFO, "No products are available!", ""));
//        }
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

    private String getCategoryEnumAsStringClass(CategoryEnum category) {
        String categoryType = "";

        switch (category) {
            case ENDOWMENT:
                categoryType = "EndowmentEntity";
                break;

            case TERMLIFE:
                categoryType = "TermLifeProductEntity";
                break;

            case WHOLELIFE:
                categoryType = "WholeLifeProductEntity";
                break;

            default:
                break;
        }
        return categoryType;
    }

    public void redirectToViewProductPage(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetail.xhtml");
    }

    public List<CategoryEnum> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEnum> categories) {
        this.categories = categories;
    }

    public CategoryEnum getFilterCategory() {
        return filterCategory;
    }

    public void setFilterCategory(CategoryEnum filterCategory) {
        this.filterCategory = filterCategory;
    }

    public Integer getFilterCoverageTerm() {
        return filterCoverageTerm;
    }

    public void setFilterCoverageTerm(Integer filterCoverageTerm) {
        this.filterCoverageTerm = filterCoverageTerm;
    }

    public Integer getFilterPremiumTerm() {
        return filterPremiumTerm;
    }

    public void setFilterPremiumTerm(Integer filterPremiumTerm) {
        this.filterPremiumTerm = filterPremiumTerm;
    }

    public BigDecimal getFilterSumAssured() {
        return filterSumAssured;
    }

    public void setFilterSumAssured(BigDecimal filterSumAssured) {
        this.filterSumAssured = filterSumAssured;
    }

    public ProductSessionBeanLocal getProductSessionBean() {
        return productSessionBean;
    }

    public void setProductSessionBean(ProductSessionBeanLocal productSessionBean) {
        this.productSessionBean = productSessionBean;
    }

    public List<ProductEntityWrapper> getListOfProducts() {
        return listOfProducts;
    }

    public void setListOfProducts(List<ProductEntityWrapper> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public ProductEntityWrapper getProductToView() {
        return productToView;
    }

    public void setProductToView(ProductEntityWrapper productToView) {
        this.productToView = productToView;
    }

    public List<ProductEntityWrapper> getFilteredProducts() {
        return filteredProducts;
    }

    public void setFilteredProducts(List<ProductEntityWrapper> filteredProducts) {
        this.filteredProducts = filteredProducts;
    }

    public List<ProductEntityWrapper> getSearchedProducts() {
        return searchedProducts;
    }

    public void setSearchedProducts(List<ProductEntityWrapper> searchedProducts) {
        this.searchedProducts = searchedProducts;
    }

    public ViewProductDetailManagedBean getViewProductDetailManagedBean() {
        return viewProductDetailManagedBean;
    }

    public void setViewProductDetailManagedBean(ViewProductDetailManagedBean viewProductDetailManagedBean) {
        this.viewProductDetailManagedBean = viewProductDetailManagedBean;
    }

}
