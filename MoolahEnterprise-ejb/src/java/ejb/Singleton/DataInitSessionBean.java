/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.Singleton;

import ejb.entity.CompanyEntity;
import ejb.entity.PointOfContactEntity;
import ejb.stateless.CompanySessionBeanLocal;
import ejb.stateless.EmailSessionBeanLocal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CompanyAlreadyExistException;
import util.exception.CompanyCreationException;
import util.exception.CompanyDoesNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private EmailSessionBeanLocal emailSessionBean;

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    @EJB
    private CompanySessionBeanLocal companySessionBean;

    @PostConstruct
    public void dataInit() {

        if (em.find(CompanyEntity.class, 1L) == null) {
            try {
                // Create Company
                CompanyEntity newCompany = new CompanyEntity("Alibaba", "raytan96@gmail.com", "BZ8899202", "96968959", "password", BigInteger.valueOf(800L));
                companySessionBean.createAccountForCompany(newCompany);
                CompanyEntity newCompany2 = new CompanyEntity("BoboChacha", "raynnic2020@gmail.com", "BA1828371", "12345678", "password", BigInteger.valueOf(500L));
                companySessionBean.createAccountForCompany(newCompany2);
                
                //  Create Point of Contacts
                PointOfContactEntity poc = new PointOfContactEntity("Ray", "90309419", "90309419", "raytan96@gmail.com", newCompany);
                
                newCompany.getListOfPointOfContacts().add(poc);
                PointOfContactEntity poc2 = new PointOfContactEntity("Nicholas", "92921191", "65509987", "raynnic2020@gmail.com", newCompany2);
                newCompany2.getListOfPointOfContacts().add(poc2);
                
                
                
            } catch (CompanyAlreadyExistException | UnknownPersistenceException | CompanyCreationException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

}
