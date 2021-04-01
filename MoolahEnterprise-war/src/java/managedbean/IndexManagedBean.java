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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import util.exception.ProductNotFoundException;
import util.helper.ProductEntityWrapper;

/**
 *
 * @author nickg
 */
@Named(value = "indexManagedBean")
@ViewScoped
public class IndexManagedBean implements Serializable {

    @EJB
    private ProductSessionBeanLocal productSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private List<ProductEntity> listOfProduct;


    private Random rand;

    public IndexManagedBean() {
        listOfProduct = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
//        try {
        rand = new Random();
        CustomerEntity cust = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
        System.out.println("Customer ID: " + cust.getFullName());
//            listOfProduct = customerSessionBean.retrieveRecommendedProducts(cust.getCustomerId());
        listOfProduct = productSessionBean.retrieveAllFinancialProducts();
        System.out.println("Product size: " + listOfProduct.size());

//        } catch (CustomerDoesNotExistsException | ProductNotFoundException ex) {
//            System.out.println("Customer has no recommended Products! EX: " + ex.getMessage());
//        }
    }

    public List<ProductEntity> getListOfProduct() {
        return listOfProduct;
    }

    public void setListOfProduct(List<ProductEntity> listOfProduct) {
        this.listOfProduct = listOfProduct;
    }

    public void viewProduct(ActionEvent event) throws IOException {
        try {
            Long productId = (Long) event.getComponent().getAttributes().get("productId");
            ProductEntity product = productSessionBean.retrieveProductEntityById(productId);
            ProductEntityWrapper wrapper = new ProductEntityWrapper(product, getParentClassAsString(product), getChildEnumAsString(product));
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productToView", wrapper);
            System.out.println("Product ID: " + productId);
            FacesContext.getCurrentInstance().getExternalContext().redirect("product/viewProductDetail.xhtml");
        } catch (ProductNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Product cannot be found! EX: " + ex.getMessage(), null));
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
    
    

}
