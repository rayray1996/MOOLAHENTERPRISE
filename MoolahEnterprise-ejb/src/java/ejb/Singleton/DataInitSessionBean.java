/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.Singleton;

import ejb.entity.AssetEntity;
import ejb.entity.CategoryPricingEntity;
import ejb.entity.CompanyEntity;
import ejb.entity.CustomerEntity;
import ejb.entity.PointOfContactEntity;
import ejb.stateless.CompanySessionBeanLocal;
import ejb.stateless.CustomerSessionBeanLocal;
import ejb.stateless.EmailSessionBeanLocal;
import ejb.stateless.PointOfContactSessionBeanLocal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CategoryEnum;
import util.enumeration.GenderEnum;
import util.exception.CompanyAlreadyExistException;
import util.exception.CompanyCreationException;
import util.exception.CompanyDoesNotExistException;
import util.exception.CustomerAlreadyExistException;
import util.exception.CustomerCreationException;
import util.exception.InvalidPointOfContactCreationException;
import util.exception.PointOfContactAlreadyExistsException;
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
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private PointOfContactSessionBeanLocal pointOfContactSessionBean;

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
                pointOfContactSessionBean.createNewPointOfContact(poc);
                PointOfContactEntity poc2 = new PointOfContactEntity("Nicholas", "92921191", "65509987", "raynnic2020@gmail.com", newCompany2);
                pointOfContactSessionBean.createNewPointOfContact(poc2);

                // Create Customer
                GregorianCalendar dateOfBirth = new GregorianCalendar();
                dateOfBirth.set(GregorianCalendar.DATE, 14);
                dateOfBirth.set(GregorianCalendar.MONTH, 6);
                dateOfBirth.set(GregorianCalendar.YEAR, 1996);
                AssetEntity newAsset = new AssetEntity(BigDecimal.valueOf(1000), BigDecimal.valueOf(1000), BigDecimal.valueOf(3000), BigDecimal.valueOf(3000));
                CustomerEntity newCust = new CustomerEntity("Ray Tan", "raytan96@gmail.com", "12345678", dateOfBirth, "90309419", GenderEnum.MALE, Boolean.TRUE, newAsset);
                customerSessionBean.createCustomer(newCust);

                GregorianCalendar dateOfBirth2 = new GregorianCalendar();
                dateOfBirth2.set(GregorianCalendar.DATE, 21);
                dateOfBirth2.set(GregorianCalendar.MONTH, 3);
                dateOfBirth2.set(GregorianCalendar.YEAR, 1996);
                AssetEntity newAsset2 = new AssetEntity(BigDecimal.valueOf(300), BigDecimal.valueOf(1000), BigDecimal.valueOf(0), BigDecimal.valueOf(1500));
                CustomerEntity newCust2 = new CustomerEntity("Soh Qing Cai", "soh.qing.cai@gmail.com", "12345678", dateOfBirth, "93726162", GenderEnum.MALE, Boolean.FALSE, newAsset);
                customerSessionBean.createCustomer(newCust);

                // Create CategoryPricingEntity
                CategoryPricingEntity endowmentPricing = new CategoryPricingEntity(CategoryEnum.ENDOWMENT, BigInteger.valueOf(100), BigInteger.valueOf(1));
                CategoryPricingEntity termLifePricing = new CategoryPricingEntity(CategoryEnum.TERMLIFE, BigInteger.valueOf(100), BigInteger.valueOf(1));
                CategoryPricingEntity wholeLifePricing = new CategoryPricingEntity(CategoryEnum.WHOLELIFE, BigInteger.valueOf(100), BigInteger.valueOf(1));
                em.persist(endowmentPricing);
                em.persist(termLifePricing);
                em.persist(wholeLifePricing);
                
            } catch (CompanyAlreadyExistException | UnknownPersistenceException | CompanyCreationException ex) {
                System.out.println(ex.getMessage());
            } catch (PointOfContactAlreadyExistsException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidPointOfContactCreationException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CustomerAlreadyExistException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CustomerCreationException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
