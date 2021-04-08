/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.ComparisonEntity;
import ejb.entity.CustomerEntity;
import ejb.entity.ProductEntity;
import ejb.stateless.CustomerSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
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
import util.exception.CustomerDoesNotExistsException;
import util.exception.CustomerUpdateException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rayta
 */
@Named(value = "viewMySavedComparisonManagedBean")
@ViewScoped
public class ViewMySavedComparisonManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private ComparisonEntity comparisonToView;
    private String dateAdded;
    
    private List<ComparisonEntity> listOfComparisons;
    private List<ComparisonEntity> filteredComparisons;

    public ViewMySavedComparisonManagedBean() {
    }

    @PostConstruct
    public void dataInit() {
        CustomerEntity customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
        if (customer == null) {
            listOfComparisons = new ArrayList<>();
        } else {
            listOfComparisons = customer.getSavedComparisons();
            filteredComparisons = customer.getSavedComparisons();
        }
    }

    public List<ComparisonEntity> getListOfComparisons() {
        return listOfComparisons;
    }

    public void setListOfComparisons(List<ComparisonEntity> listOfComparisons) {
        this.listOfComparisons = listOfComparisons;
    }

    public List<ComparisonEntity> getFilteredComparisons() {
        return filteredComparisons;
    }

    public void setFilteredComparisons(List<ComparisonEntity> filteredComparisons) {
        this.filteredComparisons = filteredComparisons;
    }

    public void viewComparison(ActionEvent event) throws IOException {
        comparisonToView = (ComparisonEntity) event.getComponent().getAttributes().get("comparisonToView");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("comparisonToView", comparisonToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewSavedComparisonDetail.xhtml");
    }

    public void deleteComparison(ActionEvent event) {
        try {
            comparisonToView = (ComparisonEntity) event.getComponent().getAttributes().get("comparisonToDelete");
            CustomerEntity customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEntity");
            customer.getSavedComparisons().remove(comparisonToView);
            customerSessionBean.updateCustomer(customer);
            
            filteredComparisons = customer.getSavedComparisons();
            listOfComparisons = customer.getSavedComparisons();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comparison has been successfully deleted", null));
        } catch (CustomerDoesNotExistsException | UnknownPersistenceException | CustomerUpdateException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, ex.toString(), null));

        }
    }

}
