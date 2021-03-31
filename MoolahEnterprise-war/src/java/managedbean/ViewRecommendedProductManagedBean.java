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
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
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
import org.primefaces.event.SelectEvent;
import util.enumeration.CategoryEnum;
import util.enumeration.EndowmentProductEnum;
import util.enumeration.TermLifeProductEnum;
import util.enumeration.WholeLifeProductEnum;
import util.exception.InvalidFilterCriteriaException;
import util.exception.ProductNotFoundException;
import util.helper.ProductEntityWrapper;

/**
 *
 * @author rayta
 */
@Named(value = "viewRecommendedProductManagedBean")
@SessionScoped
public class ViewRecommendedProductManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private ProductSessionBeanLocal productSessionBean;

    private CustomerEntity customer;
    private ProductEntityWrapper productToView;

    private List<ProductEntityWrapper> listOfProducts;
    private List<ProductEntityWrapper> filteredProducts;
    private List<ProductEntityWrapper> searchedProducts;
    private List<String> stringParentCategory;
    private List<String> stringChildCategory;

    private String filterParentCategory;
    private Integer filterCoverageTerm;
    private Integer filterPremiumTerm;
    private BigDecimal filterSumAssured;
    private String filterChildCategory;

    public ViewRecommendedProductManagedBean() {
        listOfProducts = new ArrayList<>();
        filteredProducts = new ArrayList<>();
        searchedProducts = new ArrayList<>();
        stringParentCategory = new ArrayList<>();
        stringChildCategory = new ArrayList<>();
    }

    @PostConstruct
    public void dataInit() {
        System.out.println("dataInit viewRecommendedProducts");
        customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
//        try {
//            List<ProductEntity> tempProducts = customerSessionBean.retrieveRecommendedProducts(customer.getCustomerId());
        List<ProductEntity> tempProducts = productSessionBean.retrieveAllFinancialProducts();
        for (ProductEntity p : tempProducts) {
            listOfProducts.add(new ProductEntityWrapper(p, getParentClassAsString(p), getChildEnumAsString(p)));
            filteredProducts.add(new ProductEntityWrapper(p, getParentClassAsString(p), getChildEnumAsString(p)));
            if (!stringParentCategory.contains(getParentClassAsString(p))) {
                stringParentCategory.add(getParentClassAsString(p));
            }
        }
//        } catch (CustomerDoesNotExistsException ex) {
//            FacesContext.getCurrentInstance().addMessage("null", new FacesMessage(FacesMessage.SEVERITY_INFO, "You are not logged in!", ""));
//        } catch (ProductNotFoundException ex) {
//            FacesContext.getCurrentInstance().addMessage("null", new FacesMessage(FacesMessage.SEVERITY_INFO, "No products are available!", ""));
//        }
    }

    public void updateProductTable(SelectEvent event) {
        listOfProducts.clear();
        filteredProducts.clear();
        stringChildCategory.clear();
        filterSumAssured = BigDecimal.valueOf(-1);
        filterCoverageTerm = -1;
        filterPremiumTerm = -1;
        filterChildCategory = "";

        switch (filterParentCategory) {
            case "Endowment":
                stringChildCategory.add("Endowment");

                List<EndowmentEntity> tempE = productSessionBean.retrieveAllEndowmentProducts();
                for (EndowmentEntity e : tempE) {
                    listOfProducts.add(new ProductEntityWrapper(e, getParentClassAsString(e), getChildEnumAsString(e)));
                    filteredProducts.add(new ProductEntityWrapper(e, getParentClassAsString(e), getChildEnumAsString(e)));
                }
                break;

            case "Term Life":
                stringChildCategory.add("Accident");
                stringChildCategory.add("Critical Illness");
                stringChildCategory.add("Hospital");

                List<TermLifeProductEntity> tempT = productSessionBean.retrieveAllTermLifeProducts();
                for (TermLifeProductEntity t : tempT) {
                    listOfProducts.add(new ProductEntityWrapper(t, getParentClassAsString(t), getChildEnumAsString(t)));
                    filteredProducts.add(new ProductEntityWrapper(t, getParentClassAsString(t), getChildEnumAsString(t)));
                }
                break;

            case "Whole Life":
                stringChildCategory.add("Accident");
                stringChildCategory.add("Critical Illness");
                stringChildCategory.add("Hospital");
                stringChildCategory.add("Life Insurance");

                List<WholeLifeProductEntity> tempW = productSessionBean.retrieveAllWholeLifeProducts();
                for (WholeLifeProductEntity w : tempW) {
                    listOfProducts.add(new ProductEntityWrapper(w, getParentClassAsString(w), getChildEnumAsString(w)));
                    filteredProducts.add(new ProductEntityWrapper(w, getParentClassAsString(w), getChildEnumAsString(w)));
                }
                break;
            default:
                System.out.println("nothing");
                List<ProductEntity> tempProducts = productSessionBean.retrieveAllFinancialProducts();
                for (ProductEntity p : tempProducts) {
                    listOfProducts.add(new ProductEntityWrapper(p, getParentClassAsString(p), getChildEnumAsString(p)));
                    filteredProducts.add(new ProductEntityWrapper(p, getParentClassAsString(p), getChildEnumAsString(p)));
                    if (!stringParentCategory.contains(getParentClassAsString(p))) {
                        stringParentCategory.add(getParentClassAsString(p));
                    }
                }
                break;
        }
    }

    public void retrieveFilteredProducts(SelectEvent event) {
        try {
            CategoryEnum parentCategory = null;
            listOfProducts.clear();
            filteredProducts.clear();
            // get categoryenum
            switch (filterParentCategory) {
                case "Endowment":
                    parentCategory = CategoryEnum.ENDOWMENT;
                    break;
                case "Term Life":
                    parentCategory = CategoryEnum.TERMLIFE;
                    break;
                case "Whole Life":
                    parentCategory = CategoryEnum.WHOLELIFE;
                    break;
                default:
                    break;
            }
            
            // get parent enum
            EndowmentProductEnum endowmentChildCategory = null;
            TermLifeProductEnum termLifeChildCategory = null;
            WholeLifeProductEnum wholeLifeChildCategory = null;
            
            switch (parentCategory) {
                case ENDOWMENT:
                    if (filterChildCategory.equals("Endowment")) {
                        endowmentChildCategory = EndowmentProductEnum.ENDOWMENT;
                    }
                    break;
                case TERMLIFE:
                    if (filterChildCategory.equals("Accident")) {
                        termLifeChildCategory = TermLifeProductEnum.ACCIDENT;
                    } else if (filterChildCategory.equals("Critical Illness")) {
                        termLifeChildCategory = TermLifeProductEnum.CRITICALILLNESS;
                    } else if (filterChildCategory.equals("Hospital")) {
                        termLifeChildCategory = TermLifeProductEnum.HOSPITAL;
                    }
                    break;
                case WHOLELIFE:
                    if (filterChildCategory.equals("Accident")) {
                        wholeLifeChildCategory = WholeLifeProductEnum.ACCIDENT;
                    } else if (filterChildCategory.equals("Critical Illness")) {
                        wholeLifeChildCategory = WholeLifeProductEnum.CRITICALILLNESS;
                    } else if (filterChildCategory.equals("Hospital")) {
                        wholeLifeChildCategory = WholeLifeProductEnum.HOSPITAL;
                    } else if (filterChildCategory.equals("Life Insurance")) {
                        wholeLifeChildCategory = WholeLifeProductEnum.LIFEINSURANCE;
                    }
                    break;
                default:
                    break;
            }
            
            // is smoker
//            Boolean isSmoker = customer.getSmoker();
            Boolean isSmoker = false;
            
            List<ProductEntity> resultProducts = new ArrayList<>();
            System.out.println("parentCategory = " + parentCategory.toString() + "\n"
            + "");
            resultProducts = productSessionBean.filterProductsByCriteria(parentCategory, null, isSmoker, filterSumAssured, filterCoverageTerm, filterPremiumTerm, endowmentChildCategory, termLifeChildCategory, wholeLifeChildCategory);
            for (ProductEntity r : resultProducts) {
                listOfProducts.add(new ProductEntityWrapper(r, getParentClassAsString(r), getChildEnumAsString(r)));
                filteredProducts.add(new ProductEntityWrapper(r, getParentClassAsString(r), getChildEnumAsString(r)));
                
            }
        } catch (InvalidFilterCriteriaException | ProductNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, ex.toString(), null));
        }
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

    public void redirectToViewProductPage(ActionEvent event) {
        System.out.println("redirect page");
        ProductEntityWrapper temp = (ProductEntityWrapper) event.getComponent().getAttributes().get("productToView");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productToView", temp);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetail.xhtml");
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Link is faulty", null));
        }
    }

    public List<String> getStringParentCategory() {
        return stringParentCategory;
    }

    public void setStringParentCategory(List<String> stringParentCategory) {
        this.stringParentCategory = stringParentCategory;
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

    public String getFilterParentCategory() {
        return filterParentCategory;
    }

    public void setFilterParentCategory(String filterParentCategory) {
        this.filterParentCategory = filterParentCategory;
    }

    public String getFilterChildCategory() {
        return filterChildCategory;
    }

    public void setFilterChildCategory(String filterChildCategory) {
        this.filterChildCategory = filterChildCategory;
    }

    public List<String> getStringChildCategory() {
        return stringChildCategory;
    }

    public void setStringChildCategory(List<String> stringChildCategory) {
        this.stringChildCategory = stringChildCategory;
    }
}
