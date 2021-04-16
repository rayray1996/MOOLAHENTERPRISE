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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sohqi
 */
public class CompanyCreateWrapper {

    private CompanyEntity companyEntity;
    private List<PointOfContactEntity> listOfPointOfContacts;

    public CompanyCreateWrapper() {
    }

    public CompanyCreateWrapper(CompanyEntity companyEntity, List<PointOfContactEntity> listOfPointOfContacts) {
        this.companyEntity = companyEntity;
        this.listOfPointOfContacts = listOfPointOfContacts;
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
}
