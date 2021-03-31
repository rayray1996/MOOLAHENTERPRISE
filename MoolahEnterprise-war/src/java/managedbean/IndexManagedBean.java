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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CustomerDoesNotExistsException;
import util.exception.ProductNotFoundException;

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

    private ProductEntity currProd1;

    private ProductEntity currProd2;

    private ProductEntity currProd3;

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
        Long productId = (Long) event.getComponent().getAttributes().get("productId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productId", productId);
        System.out.println("Product ID: " + productId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml"); // to replace
    }

    public ProductEntity getCurrProd1() {
        int maxSize = listOfProduct.size() - 1;
        int index = rand.nextInt(maxSize);
        System.out.println("Index: " + index);
        currProd1 = listOfProduct.get(index);
        return currProd1;
    }

    public void setCurrProd1(ProductEntity currProd1) {
        this.currProd1 = currProd1;
    }

    public ProductEntity getCurrProd2() {
        int maxSize = listOfProduct.size() - 1;
        int index = rand.nextInt(maxSize);
        System.out.println("Index: " + index);
        currProd2 = listOfProduct.get(index);
        if (!currProd2.getProductId().equals(currProd1.getProductId())) {
            return currProd2;
        } else {
            maxSize = listOfProduct.size() - 1;
            index = rand.nextInt(maxSize);
            System.out.println("Index: " + index);
            currProd2 = listOfProduct.get(index);
            return currProd2;
        }
    }

    public void setCurrProd2(ProductEntity currProd2) {
        this.currProd2 = currProd2;
    }

    public ProductEntity getCurrProd3() {
        int maxSize = listOfProduct.size() - 1;
        int index = rand.nextInt(maxSize);
        System.out.println("Index: " + index);
        currProd3 = listOfProduct.get(index);
        if (!currProd2.getProductId().equals(currProd1.getProductId()) && !currProd3.getProductId().equals(currProd2.getProductId())) {
            return currProd3;
        } else {
            maxSize = listOfProduct.size() - 1;
            index = rand.nextInt(maxSize);
            System.out.println("Index: " + index);
            currProd3 = listOfProduct.get(index);
            return currProd3;
        }
    }

    public void setCurrProd3(ProductEntity currProd3) {
        this.currProd3 = currProd3;

    }

}
