/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.CompanyEntity;
import ejb.entity.CustomerEntity;
import ejb.entity.PremiumEntity;
import ejb.entity.ProductEntity;
import ejb.stateless.ClickthroughSessionBeanLocal;
import ejb.stateless.CompanySessionBeanLocal;
import ejb.stateless.CustomerSessionBeanLocal;
import ejb.stateless.PremiumSessionBeanLocal;
import ejb.stateless.ProductSessionBeanLocal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CompanyDoesNotExistException;
import util.exception.CustomerDoesNotExistsException;
import util.exception.CustomerUpdateException;
import util.exception.InvalidProductCreationException;
import util.exception.ProductAlreadyExistsException;
import util.exception.ProductIsDeletedException;
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;
import util.helper.AffordabilityWrapper;
import util.helper.ProductEntityWrapper;

/**
 *
 * @author rayta
 */
@Named(value = "viewProductDetailManagedBean")
@ViewScoped
public class ViewProductDetailManagedBean implements Serializable {

    @EJB
    private ClickthroughSessionBeanLocal clickthroughSessionBean;

    @EJB
    private PremiumSessionBeanLocal premiumSessionBean;

    @EJB
    private CompanySessionBeanLocal companySessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private ProductSessionBeanLocal productSessionBean;

    private CustomerEntity cust;

    private ProductEntityWrapper productToView;

    private ProductEntity product;

    private Boolean customerLiked;

    private Long startTime;

    private Float endTime;

    private List<BigDecimal> listOfAssetCust;

    private List<AffordabilityWrapper> listOfAffordability;

    private Boolean hasCounted;

    public ViewProductDetailManagedBean() {
        listOfAffordability = new ArrayList<>();
    }

    @PostConstruct
    public void dataInit() {
        try {
            productToView = (ProductEntityWrapper) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productToView");
            if (productToView == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Product not found", null));
                return;
            }
            product = productSessionBean.retrieveProductEntityById(productToView.getProductEntity().getProductId());

            cust = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
            cust = customerSessionBean.retrieveCustomerById(cust.getCustomerId());
            listOfAssetCust = customerSessionBean.getThreeYearsOfCapital(cust.getCustomerId());

            List<PremiumEntity> listOfPremiums = new ArrayList<>();
            if (cust.getSmoker()) {
                if (product.getIsAvailableToSmokers() == true) {
                    if (!product.getListOfSmokerPremium().isEmpty()) {
                        System.out.println("Smoker premium special");
                        listOfPremiums = product.getListOfSmokerPremium();
                    } else {
                        System.out.println("Smoker premium same");
                        listOfPremiums = product.getListOfPremium();
                        product.setListOfSmokerPremium(listOfPremiums);
                    }
                } else {
                    listOfPremiums = product.getListOfPremium();
                    product.setListOfSmokerPremium(listOfPremiums);
                }
            } else {
                System.out.println("Check entry non smoker");
                listOfPremiums = product.getListOfPremium();
            }

            int dobYear = customerSessionBean.getAgeOfCustomer(cust);

            for (PremiumEntity p : listOfPremiums) {
                System.out.println(p.getMinAgeGroup() + ", " + p.getMaxAgeGroup() + "\n");
            }

            System.out.println("Date of birth: " + dobYear);
            System.out.println("List of premium size: " + listOfPremiums.size());

            List<PremiumEntity> custPremiums = new ArrayList<>();

            for (PremiumEntity premium : listOfPremiums) {
                if (((dobYear >= premium.getMinAgeGroup() && dobYear <= premium.getMaxAgeGroup())
                        || (!premium.getMinAgeGroup().equals(premium.getMaxAgeGroup()) && (dobYear >= premium.getMinAgeGroup() || dobYear <= premium.getMaxAgeGroup()))) && custPremiums.size() < 3) {
                    custPremiums.add(premium);
                    dobYear++;
                    System.out.println("Premiums added: " + premium);
                }
            }

            AffordabilityWrapper tempWrapper = new AffordabilityWrapper();
            AffordabilityWrapper tempWrapper2 = new AffordabilityWrapper();
            AffordabilityWrapper tempWrapper3 = new AffordabilityWrapper();

            if (custPremiums.get(0) != null) {
                tempWrapper = new AffordabilityWrapper(listOfAssetCust.get(0), listOfAssetCust.get(0).subtract(custPremiums.get(0).getPremiumValue()));
            } else {
                tempWrapper = new AffordabilityWrapper(new BigDecimal("-1"), new BigDecimal("-1"));
            }
            listOfAffordability.add(tempWrapper);

            if (custPremiums.get(1) != null) {
                tempWrapper2 = new AffordabilityWrapper(listOfAssetCust.get(1).subtract(custPremiums.get(0).getPremiumValue()), listOfAssetCust.get(1).subtract(custPremiums.get(0).getPremiumValue()).subtract(custPremiums.get(1).getPremiumValue()));
            } else {
                tempWrapper = new AffordabilityWrapper(new BigDecimal("-1"), new BigDecimal("-1"));
            }

            listOfAffordability.add(tempWrapper2);

            if (custPremiums.get(2) != null) {
                tempWrapper3 = new AffordabilityWrapper(listOfAssetCust.get(2).subtract(custPremiums.get(0).getPremiumValue()).subtract(custPremiums.get(1).getPremiumValue()), listOfAssetCust.get(2).subtract(custPremiums.get(0).getPremiumValue()).subtract(custPremiums.get(1).getPremiumValue()).subtract(custPremiums.get(2).getPremiumValue()));
            } else {
                tempWrapper3 = new AffordabilityWrapper(new BigDecimal("-1"), new BigDecimal("-1"));
            }

            listOfAffordability.add(tempWrapper3);

            customerLikeProduct();
            hasCounted = false;

        } catch (ProductNotFoundException | CustomerDoesNotExistsException | ProductIsDeletedException ex) {
            System.out.println("Product does not exists!");
        }
    }

    public void likeProduct(ActionEvent event) {
        try {
//            cust = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
//            cust = customerSessionBean.retrieveCustomerById(cust.getCustomerId());
            if (cust.getListOfLikeProducts().contains(product)) {
                cust = customerSessionBean.retrieveCustomerById(cust.getCustomerId());
                cust.getListOfLikeProducts().remove(product);
                customerSessionBean.updateCustomer(cust);
                cust = customerSessionBean.retrieveCustomerById(cust.getCustomerId());
                customerLikeProduct();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product has been removed from your profile!", null));
            } else {
                customerSessionBean.likeAProduct(cust.getCustomerId(), product.getProductId());
                cust = customerSessionBean.retrieveCustomerById(cust.getCustomerId());
                customerLikeProduct();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product has been saved to your profile!", null));
            }

        } catch (CustomerDoesNotExistsException | ProductNotFoundException | UnknownPersistenceException | CustomerUpdateException | ProductIsDeletedException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    public void addCount() {
        if (!hasCounted) {
            try {
                clickthroughSessionBean.addClickToProduct(product.getProductId());
                System.out.println("EndTime Prod ID: " + product.getProductId());
                product = productSessionBean.retrieveProductEntityById(productToView.getProductEntity().getProductId());

                System.out.println("New Counter: " + product.getClickThroughInfo().getMonthCounter());
                productSessionBean.updateProductListing(product);
                hasCounted = true;
            } catch (ProductAlreadyExistsException | UnknownPersistenceException | InvalidProductCreationException | ProductNotFoundException | ProductIsDeletedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public ProductEntityWrapper getProductToView() {
        return productToView;
    }

    public void setProductToView(ProductEntityWrapper productToView) {
        this.productToView = productToView;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public void customerLikeProduct() {
        try {
            CustomerEntity cust = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
            cust = customerSessionBean.retrieveCustomerById(cust.getCustomerId());
            if (cust.getListOfLikeProducts().contains(product)) {
                customerLiked = true;
            } else {
                customerLiked = false;
            }
        } catch (CustomerDoesNotExistsException ex) {
            Logger.getLogger(ViewProductDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean getCustomerLiked() {
        return customerLiked;
    }

    public void setCustomerLiked(Boolean customerLiked) {
        this.customerLiked = customerLiked;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Float getEndTime() {
        return endTime;
    }

    public void setEndTime(Float endTime) {
        this.endTime = endTime;
    }

    public List<BigDecimal> getListOfAssetCust() {
        return listOfAssetCust;
    }

    public void setListOfAssetCust(List<BigDecimal> listOfAssetCust) {
        this.listOfAssetCust = listOfAssetCust;
    }

    public List<AffordabilityWrapper> getListOfAffordability() {
        return listOfAffordability;
    }

    public void setListOfAffordability(List<AffordabilityWrapper> listOfAffordability) {
        this.listOfAffordability = listOfAffordability;
    }

    public CustomerEntity getCust() {
        return cust;
    }

    public void setCust(CustomerEntity cust) {
        this.cust = cust;
    }

    public Boolean getHasCounted() {
        return hasCounted;
    }

    public void setHasCounted(Boolean hasCounted) {
        this.hasCounted = hasCounted;
    }
}
