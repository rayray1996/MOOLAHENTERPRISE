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
import ejb.stateless.PremiumSessionBeanLocal;
import ejb.stateless.ProductSessionBeanLocal;
import ejb.stateless.RiderSessionBeanLocal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
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
import util.enumeration.PolicyCurrencyEnum;
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
import util.exception.PremiumAlreadyExistException;
import util.exception.PremiumCreationException;
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
    private PremiumSessionBeanLocal premiumSessionBean;

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
                CompanyEntity alibabaCompany = new CompanyEntity("Alibaba", "raytan96@gmail.com", "BZ8899202", "96968959", "password", BigInteger.valueOf(800L));
                alibabaCompany = companySessionBean.createAccountForCompany(alibabaCompany);
                CompanyEntity tencentCompany = new CompanyEntity("Tencent", "raynnic2020@gmail.com", "BA1828371", "12345678", "password", BigInteger.valueOf(500L));
                tencentCompany = companySessionBean.createAccountForCompany(tencentCompany);

                //  Create Point of Contacts
                PointOfContactEntity poc = new PointOfContactEntity("Ray", "90309419", "90309419", "raytan96@gmail.com", alibabaCompany);
                pointOfContactSessionBean.createNewPointOfContact(poc);
                PointOfContactEntity poc2 = new PointOfContactEntity("Nicholas", "92921191", "65509987", "raynnic2020@gmail.com", tencentCompany);
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

                //Create ProductEntity - using specific subclasses
                //EndowmentProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, Boolean isDeleted, Integer premiumTerm) {
                ProductEntity endowmentEntity = new EndowmentEntity(EndowmentProductEnum.ENDOWMENT, "Alibaba Endowment Product 01", 103, BigDecimal.valueOf(100000.00), "This is an Endowment Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.\n This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 50, PolicyCurrencyEnum.SGD);
                endowmentEntity = productSessionBean.createProductListing(endowmentEntity, alibabaCompany.getCompanyId());

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity rider01 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Alibaba Endowment Product 01");
                rider01 = riderSessionBean.createRider(rider01, endowmentEntity.getProductId());

                //Description
                FeatureEntity feature01 = new FeatureEntity("This additional feature is for Alibaba Endowment Product 01");
                feature01 = featureSessionBean.createNewFeature(feature01, endowmentEntity.getProductId());

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                List<PremiumEntity> listOfPremium = new ArrayList<>();
                listOfPremium.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520)));
                listOfPremium.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.valueOf(5040)));
                listOfPremium.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.valueOf(7560)));
                listOfPremium.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.valueOf(10080)));
                listOfPremium.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.valueOf(12600)));
                listOfPremium.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.valueOf(15120)));
                listOfPremium.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.valueOf(17640)));
                listOfPremium.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.valueOf(20160)));
                listOfPremium.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.valueOf(22680)));
                listOfPremium.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.valueOf(25200)));
                listOfPremium.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.valueOf(27720)));
                listOfPremium.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.valueOf(30240)));
                listOfPremium.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.valueOf(32760)));
                listOfPremium.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.valueOf(35280)));
                listOfPremium.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.valueOf(37800)));
                listOfPremium.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.valueOf(40320)));
                listOfPremium.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.valueOf(42840)));
                listOfPremium.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.valueOf(45360)));
                
                for(PremiumEntity premium : listOfPremium){
                    premium = premiumSessionBean.createNewPremiumEntity(premium, endowmentEntity.getProductId());
                }
                
                
                 //Create ProductEntity - using specific subclasses
                //EndowmentProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, Boolean isDeleted, Integer premiumTerm) {
                ProductEntity endowmentEntity02 = new EndowmentEntity(EndowmentProductEnum.ENDOWMENT, "Alibaba Endowment Product 02", 80, BigDecimal.valueOf(1000000.00), "This is an Endowment Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.\n This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 50, PolicyCurrencyEnum.SGD);
                endowmentEntity02 = productSessionBean.createProductListing(endowmentEntity02, alibabaCompany.getCompanyId());

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity rider02 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Alibaba Endowment Product 02");
                rider02 = riderSessionBean.createRider(rider02, endowmentEntity02.getProductId());
                
                
                
                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                List<PremiumEntity> listOfPremium02 = new ArrayList<>();
                listOfPremium02.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520)));
                listOfPremium02.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.valueOf(5040)));
                listOfPremium02.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.valueOf(7560)));
                listOfPremium02.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.valueOf(10080)));
                listOfPremium02.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.valueOf(12600)));
                listOfPremium02.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.valueOf(15120)));
                listOfPremium02.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.valueOf(17640)));
                listOfPremium02.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.valueOf(20160)));
                listOfPremium02.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.valueOf(22680)));
                listOfPremium02.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.valueOf(25200)));
                listOfPremium02.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.valueOf(27720)));
                listOfPremium02.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.valueOf(30240)));
                listOfPremium02.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.valueOf(32760)));
                listOfPremium02.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.valueOf(35280)));
                listOfPremium02.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.valueOf(37800)));
                listOfPremium02.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.valueOf(40320)));
                listOfPremium02.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.valueOf(42840)));
                listOfPremium02.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.valueOf(45360)));
                
                
                
                
                
              
                
                 //Create ProductEntity - using specific subclasses
                //EndowmentProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, Boolean isDeleted, Integer premiumTerm) {
                ProductEntity endowmentEntity03 = new EndowmentEntity(EndowmentProductEnum.ENDOWMENT, "Alibaba Endowment Product 03", 80, BigDecimal.valueOf(1000000.00), "This is an Endowment Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.\n This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 50, PolicyCurrencyEnum.SGD);
                endowmentEntity03 = productSessionBean.createProductListing(endowmentEntity03, alibabaCompany.getCompanyId());

                
                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                List<PremiumEntity> listOfPremium03 = new ArrayList<>();
                listOfPremium03.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520)));
                listOfPremium03.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.valueOf(5040)));
                listOfPremium03.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.valueOf(7560)));
                listOfPremium03.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.valueOf(10080)));
                listOfPremium03.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.valueOf(12600)));
                listOfPremium03.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.valueOf(15120)));
                listOfPremium03.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.valueOf(17640)));
                listOfPremium03.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.valueOf(20160)));
                listOfPremium03.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.valueOf(22680)));
                listOfPremium03.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.valueOf(25200)));
                listOfPremium03.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.valueOf(27720)));
                listOfPremium03.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.valueOf(30240)));
                listOfPremium03.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.valueOf(32760)));
                listOfPremium03.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.valueOf(35280)));
                listOfPremium03.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.valueOf(37800)));
                listOfPremium03.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.valueOf(40320)));
                listOfPremium03.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.valueOf(42840)));
                listOfPremium03.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.valueOf(45360)));
                
                
                
                
                
                
                 //TermLifeProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, 
                //Boolean isDeleted, Integer premiumTerm) {
                ProductEntity termLifeEntity = new TermLifeProductEntity(TermLifeProductEnum.ACCIDENT, "Alibaba Term Life Product 02", 40, BigDecimal.valueOf(10000), 
                        "This is an Term Life Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.", false, 50, PolicyCurrencyEnum.SGD);
                termLifeEntity = productSessionBean.createProductListing(termLifeEntity, alibabaCompany.getCompanyId());
                
                
                
                
                /*
                 * For Company 2
                 */
                //EndowmentProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, Boolean isDeleted, Integer premiumTerm) {
                ProductEntity coy2EndowmentEntity01 = new EndowmentEntity(EndowmentProductEnum.ENDOWMENT, "Tencent Endowment Product 01", 103, BigDecimal.valueOf(100000.00), "This is an Endowment Product by Tencent.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.\n This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100, PolicyCurrencyEnum.SGD);
                coy2EndowmentEntity01 = productSessionBean.createProductListing(coy2EndowmentEntity01, tencentCompany.getCompanyId());

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity coy2rider01 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Tencent Endowment Product 01");
                coy2rider01 = riderSessionBean.createRider(coy2rider01, coy2EndowmentEntity01.getProductId());

                //Description
                FeatureEntity coy2feature01 = new FeatureEntity("This additional feature is for Tencent Endowment Product 01");
                coy2feature01 = featureSessionBean.createNewFeature(coy2feature01, coy2EndowmentEntity01.getProductId());

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                List<PremiumEntity> coy2ListOfPremium = new ArrayList<>();
                coy2ListOfPremium.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520)));
                coy2ListOfPremium.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.valueOf(5040)));
                coy2ListOfPremium.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.valueOf(7560)));
                coy2ListOfPremium.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.valueOf(10080)));
                coy2ListOfPremium.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.valueOf(12600)));
                coy2ListOfPremium.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.valueOf(15120)));
                coy2ListOfPremium.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.valueOf(17640)));
                coy2ListOfPremium.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.valueOf(20160)));
                coy2ListOfPremium.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.valueOf(22680)));
                coy2ListOfPremium.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.valueOf(25200)));
                coy2ListOfPremium.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.valueOf(27720)));
                coy2ListOfPremium.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.valueOf(30240)));
                coy2ListOfPremium.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.valueOf(32760)));
                coy2ListOfPremium.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.valueOf(35280)));
                coy2ListOfPremium.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.valueOf(37800)));
                coy2ListOfPremium.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.valueOf(40320)));
                coy2ListOfPremium.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.valueOf(42840)));
                coy2ListOfPremium.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.valueOf(45360)));
                
                for(PremiumEntity premium : coy2ListOfPremium){
                    premium = premiumSessionBean.createNewPremiumEntity(premium, coy2EndowmentEntity01.getProductId());
                }

             

            } catch (CompanyAlreadyExistException | UnknownPersistenceException | CompanyCreationException | PointOfContactAlreadyExistsException
                    | InvalidPointOfContactCreationException | CustomerAlreadyExistException | CustomerCreationException | ProductAlreadyExistsException | RiderAlreadyExistException
                    | RiderCreationException | ProductNotFoundException | FeatureAlreadyExistsException | FeatureCreationException | InvalidProductCreationException | PremiumAlreadyExistException 
                    | PremiumCreationException ex) {
                System.out.println(ex.getMessage());
            } 
        }

    }

}
