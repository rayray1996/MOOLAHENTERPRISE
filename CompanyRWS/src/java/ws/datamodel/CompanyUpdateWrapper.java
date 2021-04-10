/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import ejb.entity.CompanyEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.PointOfContactEntity;
import ejb.entity.ProductEntity;
import ejb.entity.RefundEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 *
 * @author sohqi
 */
public class CompanyUpdateWrapper {

    private CompanyEntity companyEntity;
    private List<PointOfContactEntity> listOfPointOfContacts;
    private RefundEntity refund;

    private List<PaymentEntity> listOfPayments;

    private List<ProductEntity> listOfProducts;

    public CompanyUpdateWrapper() {
        listOfPayments = new ArrayList<PaymentEntity>();
        listOfProducts = new ArrayList<ProductEntity>();
        listOfPointOfContacts = new ArrayList<PointOfContactEntity>();
        refund = new RefundEntity();
        companyEntity = new CompanyEntity();
    }

    public CompanyUpdateWrapper(CompanyEntity companyEntity, List<PointOfContactEntity> listOfPointOfContacts, RefundEntity refund, List<PaymentEntity> listOfPayments, List<ProductEntity> listOfProducts) {
        this.companyEntity = companyEntity;
        this.listOfPointOfContacts = listOfPointOfContacts;
        this.listOfPayments = listOfPayments;
        this.listOfProducts = listOfProducts;
        this.refund = refund;

    }

    public RefundEntity getRefund() {
        return refund;
    }

    public void setRefund(RefundEntity refund) {
        this.refund = refund;
    }

    public List<PaymentEntity> getListOfPayments() {
        return listOfPayments;
    }

    public void setListOfPayments(List<PaymentEntity> listOfPayments) {
        this.listOfPayments = listOfPayments;
    }

    public List<ProductEntity> getListOfProducts() {
        return listOfProducts;
    }

    public void setListOfProducts(List<ProductEntity> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    public void setCompanyEntity(CompanyEntity companyEntity) {
        this.companyEntity = companyEntity;
    }

    public List<PointOfContactEntity> getListOfPointOfContacts() {
        return listOfPointOfContacts;
    }

    public void setListOfPointOfContacts(List<PointOfContactEntity> listOfPointOfContacts) {
        this.listOfPointOfContacts = listOfPointOfContacts;
    }

    public void setID() {
        if (listOfPayments != null && !listOfPayments.isEmpty()) {
            for (PaymentEntity pay : listOfPayments) {
                pay.setCompany(companyEntity);
            }
        }
        if (listOfPointOfContacts != null && !listOfPointOfContacts.isEmpty()) {
            for (PointOfContactEntity poc : listOfPointOfContacts) {
                poc.setCompany(companyEntity);
            }
        }
        if (listOfProducts != null && !listOfProducts.isEmpty()) {
            for (ProductEntity product : listOfProducts) {
                product.setCompany(companyEntity);
            }
        }
        if (refund != null) {
            refund.setCompany(companyEntity);
        }
    }
}
