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
import ejb.entity.EndowmentEntity;
import ejb.entity.FeatureEntity;
import ejb.entity.PointOfContactEntity;
import ejb.entity.PremiumEntity;
import ejb.entity.ProductEntity;
import ejb.entity.RiderEntity;
import ejb.entity.TermLifeProductEntity;
import ejb.stateless.CompanySessionBeanLocal;
import ejb.stateless.CustomerSessionBeanLocal;
import ejb.stateless.EmailSessionBeanLocal;
import ejb.stateless.FeatureSessionBeanLocal;
import ejb.stateless.PointOfContactSessionBeanLocal;
import ejb.stateless.ProductSessionBeanLocal;
import ejb.stateless.RiderSessionBeanLocal;
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
import util.enumeration.EndowmentProductEnum;
import util.enumeration.GenderEnum;
import util.enumeration.TermLifeProductEnum;
import util.exception.CompanyAlreadyExistException;
import util.exception.CompanyCreationException;
import util.exception.CompanyDoesNotExistException;
import util.exception.CustomerAlreadyExistException;
import util.exception.CustomerCreationException;
import util.exception.FeatureAlreadyExistsException;
import util.exception.FeatureCreationException;
import util.exception.InvalidPointOfContactCreationException;
import util.exception.InvalidProductCreationException;
import util.exception.PointOfContactAlreadyExistsException;
import util.exception.ProductAlreadyExistsException;
import util.exception.ProductNotFoundException;
import util.exception.RiderAlreadyExistException;
import util.exception.RiderCreationException;
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
    private FeatureSessionBeanLocal featureSessionBean;

    @EJB
    private ProductSessionBeanLocal productSessionBean;

    @EJB
    private RiderSessionBeanLocal riderSessionBean;

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
                newCompany = companySessionBean.createAccountForCompany(newCompany);
                CompanyEntity newCompany2 = new CompanyEntity("Tencent", "raynnic2020@gmail.com", "BA1828371", "12345678", "password", BigInteger.valueOf(500L));
                newCompany2 = companySessionBean.createAccountForCompany(newCompany2);

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
                CustomerEntity newCust = new CustomerEntity("Ray Tan", "raytan96@gmail.com", "12345678", dateOfBirth, "90309419", GenderEnum.MALE, Boolean.TRUE, newAsset, false);
                customerSessionBean.createCustomer(newCust);

                GregorianCalendar dateOfBirth2 = new GregorianCalendar();
                dateOfBirth2.set(GregorianCalendar.DATE, 21);
                dateOfBirth2.set(GregorianCalendar.MONTH, 3);
                dateOfBirth2.set(GregorianCalendar.YEAR, 1996);
                AssetEntity newAsset2 = new AssetEntity(BigDecimal.valueOf(300), BigDecimal.valueOf(1000), BigDecimal.valueOf(0), BigDecimal.valueOf(1500));
                CustomerEntity newCust2 = new CustomerEntity("Soh Qing Cai", "soh.qing.cai@gmail.com", "12345678", dateOfBirth, "93726162", GenderEnum.MALE, Boolean.FALSE, newAsset2, false);
                customerSessionBean.createCustomer(newCust2);

                // Create CategoryPricingEntity
                CategoryPricingEntity endowmentPricing = new CategoryPricingEntity(CategoryEnum.ENDOWMENT, BigInteger.valueOf(100), BigInteger.valueOf(1));
                CategoryPricingEntity termLifePricing = new CategoryPricingEntity(CategoryEnum.TERMLIFE, BigInteger.valueOf(100), BigInteger.valueOf(1));
                CategoryPricingEntity wholeLifePricing = new CategoryPricingEntity(CategoryEnum.WHOLELIFE, BigInteger.valueOf(100), BigInteger.valueOf(1));
                em.persist(endowmentPricing);
                em.persist(termLifePricing);
                em.persist(wholeLifePricing);

                //Create ProductEntity - using specific subclasses
                //EndowmentProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, Boolean isDeleted, Integer premiumTerm) {
                ProductEntity endowmentEntity = new EndowmentEntity(EndowmentProductEnum.ENDOWMENT, "Alibaba Endowment Product 01", 103, BigDecimal.valueOf(100000.00), "This is an Endowment Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.\n This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100);
                endowmentEntity = productSessionBean.createProductListing(endowmentEntity, newCompany.getCompanyId());
                
//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity rider01 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Alibaba Endowment Product 01");
                rider01 = riderSessionBean.createRider(rider01, endowmentEntity.getProductId());

                //Description
                FeatureEntity feature01 = new FeatureEntity("This additional feature is for Alibaba Endowment Product 01");
                feature01 = featureSessionBean.createNewFeature(feature01, endowmentEntity.getProductId());
                
                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
//                List<PremiumEntity> listOfPremium
                PremiumEntity premium01 = new PremiumEntity(18,18, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520));
                

                //TermLifeProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, 
                //Boolean isDeleted, Integer premiumTerm) {
//                TermLifeProductEntity termLifeEntity = new TermLifeProductEntity(TermLifeProductEnum.ACCIDENT, "Alibaba Term Life Product 02")



            } catch (CompanyAlreadyExistException | UnknownPersistenceException | CompanyCreationException | PointOfContactAlreadyExistsException
                    | InvalidPointOfContactCreationException | CustomerAlreadyExistException | CustomerCreationException ex) {
                System.out.println(ex.getMessage());
            
            } 
        }

    }

}
