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
import ejb.entity.WholeLifeProductEntity;
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
import util.enumeration.WholeLifeProductEnum;
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
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 50, PolicyCurrencyEnum.SGD);

                List<FeatureEntity> listOfFeature = new ArrayList<>();
                List<RiderEntity> listOfRiders = new ArrayList<>();
                List<PremiumEntity> listOfPremium = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity rider01 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Alibaba Endowment Product 01");
                listOfRiders.add(rider01);

                //Description
                FeatureEntity feature01 = new FeatureEntity("This additional feature is for Alibaba Endowment Product 01");
                listOfFeature.add(feature01);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
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

                //ProductEntity newProduct, Long companyId, List<RiderEntity> riders, List<PremiumEntity> premiums, List<FeatureEntity> features
                endowmentEntity = productSessionBean.createProductListing(endowmentEntity, alibabaCompany.getCompanyId(), listOfRiders, listOfPremium, listOfFeature);
                
                
//*****************************************************************************************************************************************************************************************************************************************************
                
                //Create ProductEntity - using specific subclasses
                //EndowmentProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, Boolean isDeleted, Integer premiumTerm) {
                ProductEntity endowmentEntity02 = new EndowmentEntity(EndowmentProductEnum.ENDOWMENT, "Alibaba Endowment Product 02", 80, BigDecimal.valueOf(1000000.00), "This is an Endowment Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.\n This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 50, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfRiders02 = new ArrayList<>();
                List<PremiumEntity> listOfPremium02 = new ArrayList<>();
                List<FeatureEntity> listOfFeatures02 = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity rider02 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Alibaba Endowment Product 02");
                listOfRiders02.add(rider02);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
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

                endowmentEntity02 = productSessionBean.createProductListing(endowmentEntity02, alibabaCompany.getCompanyId(), listOfRiders02, listOfPremium02, listOfFeatures02);

                //Create ProductEntity - using specific subclasses
                //EndowmentProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, Boolean isDeleted, Integer premiumTerm) {
                ProductEntity endowmentEntity03 = new EndowmentEntity(EndowmentProductEnum.ENDOWMENT, "Alibaba Endowment Product 03", 80, BigDecimal.valueOf(1000000.00), "This is an Endowment Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.\n This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 50, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfRiders03 = new ArrayList<>();
                List<PremiumEntity> listOfPremium03 = new ArrayList<>();
                List<FeatureEntity> listOfFeatures03 = new ArrayList<>();

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
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

                endowmentEntity03 = productSessionBean.createProductListing(endowmentEntity03, alibabaCompany.getCompanyId(), listOfRiders03, listOfPremium03, listOfFeatures03);
                
//*****************************************************************************************************************************************************************************************************************************************************
                //TermLifeProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, 
                //Boolean isDeleted, Integer premiumTerm) {
                ProductEntity termLifeEntity01 = new TermLifeProductEntity(TermLifeProductEnum.ACCIDENT, "Alibaba Term Life Product 01", 63, BigDecimal.valueOf(0),
                        "This is an Term Life Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.", false, 63, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfRiders04 = new ArrayList<>();
                List<PremiumEntity> listOfPremium04 = new ArrayList<>();
                List<FeatureEntity> listOfFeatures04 = new ArrayList<>();

                //              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity rider04 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Term Life Product 01");
                listOfRiders04.add(rider04);

                listOfPremium04.add(new PremiumEntity(23, 23, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(24, 24, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(25, 25, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(26, 26, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(27, 27, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(28, 28, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(29, 29, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(30, 30, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(31, 31, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(32, 32, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(33, 33, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(34, 34, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(35, 35, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(36, 36, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(37, 37, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(38, 38, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(39, 39, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium04.add(new PremiumEntity(40, 40, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));

                //Description
                FeatureEntity feature04 = new FeatureEntity("This additional feature is for Term Life Product 01");
                listOfFeatures04.add(feature04);

                termLifeEntity01 = productSessionBean.createProductListing(termLifeEntity01, alibabaCompany.getCompanyId(), listOfRiders04, listOfPremium04, listOfFeatures04);
                
//*****************************************************************************************************************************************************************************************************************************************************
                //TermLifeProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, 
                //Boolean isDeleted, Integer premiumTerm) {
                ProductEntity termLifeEntity02 = new TermLifeProductEntity(TermLifeProductEnum.HOSPITAL, "Alibaba Term Life Product 02", 63, BigDecimal.valueOf(0),
                        "This is an Term Life Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.", false, 63, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfRiders05 = new ArrayList<>();
                List<PremiumEntity> listOfPremium05 = new ArrayList<>();
                List<FeatureEntity> listOfFeatures05 = new ArrayList<>();

                //              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity rider05 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Term Life Product 02");
                listOfRiders05.add(rider05);

                listOfPremium05.add(new PremiumEntity(23, 23, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(24, 24, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(25, 25, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(26, 26, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(27, 27, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(28, 28, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(29, 29, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(30, 30, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(31, 31, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(32, 32, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(33, 33, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(34, 34, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(35, 35, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(36, 36, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(37, 37, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(38, 38, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(39, 39, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium05.add(new PremiumEntity(40, 40, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));

                termLifeEntity02 = productSessionBean.createProductListing(termLifeEntity02, alibabaCompany.getCompanyId(), listOfRiders05, listOfPremium05, listOfFeatures05);
                
//*****************************************************************************************************************************************************************************************************************************************************
           
                //TermLifeProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, 
                //Boolean isDeleted, Integer premiumTerm) {
                ProductEntity termLifeEntity03 = new TermLifeProductEntity(TermLifeProductEnum.CRITICALILLNESS, "Alibaba Term Life Product 03", 63, BigDecimal.valueOf(0),
                        "This is an Term Life Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.", false, 63, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfRiders06 = new ArrayList<>();
                List<PremiumEntity> listOfPremium06 = new ArrayList<>();
                List<FeatureEntity> listOfFeatures06 = new ArrayList<>();

                listOfPremium06.add(new PremiumEntity(23, 23, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(24, 24, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(25, 25, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(26, 26, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(27, 27, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(28, 28, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(29, 29, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(30, 30, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(31, 31, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(32, 32, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(33, 33, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(34, 34, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(35, 35, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(36, 36, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(37, 37, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(38, 38, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(39, 39, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));
                listOfPremium06.add(new PremiumEntity(40, 40, BigDecimal.valueOf(323), false, BigDecimal.valueOf(0)));

                termLifeEntity03 = productSessionBean.createProductListing(termLifeEntity03, alibabaCompany.getCompanyId(), listOfRiders06, listOfPremium06, listOfFeatures06);

                
 //*****************************************************************************************************************************************************************************************************************************************************
                
                //TermLifeProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, 
                //Boolean isDeleted, Integer premiumTerm) {
                ProductEntity wholeLifeEntity01 = new WholeLifeProductEntity(WholeLifeProductEnum.ACCIDENT, "Alibaba Whole Life Product 01", 78, BigDecimal.valueOf(50000),
                        "This is an Whole Life Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.", false, 20, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfRiders07 = new ArrayList<>();
                List<PremiumEntity> listOfPremium07 = new ArrayList<>();
                List<FeatureEntity> listOfFeatures07 = new ArrayList<>();

                // BigDecimal riderPremiumValue, String riderDescription
                RiderEntity rider07 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Whole Life Product 01");
                listOfRiders07.add(rider07);

                //Description
                FeatureEntity feature07 = new FeatureEntity("This additional feature is for Whole Life Product 01");
                listOfFeatures07.add(feature07);

                listOfPremium07.add(new PremiumEntity(23, 23, BigDecimal.valueOf(996), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(24, 24, BigDecimal.valueOf(1991), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(25, 25, BigDecimal.valueOf(2987), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(26, 26, BigDecimal.valueOf(3982), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(27, 27, BigDecimal.valueOf(4978), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(28, 28, BigDecimal.valueOf(5973), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(29, 29, BigDecimal.valueOf(6969), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(30, 30, BigDecimal.valueOf(7964), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(31, 31, BigDecimal.valueOf(8960), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(32, 32, BigDecimal.valueOf(9955), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(33, 33, BigDecimal.valueOf(14933), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(34, 34, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(35, 35, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(36, 36, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(37, 37, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(38, 38, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(39, 39, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium07.add(new PremiumEntity(40, 40, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));

                wholeLifeEntity01 = productSessionBean.createProductListing(wholeLifeEntity01, alibabaCompany.getCompanyId(), listOfRiders07, listOfPremium07, listOfFeatures07);

//*****************************************************************************************************************************************************************************************************************************************************                
                
                //TermLifeProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, 
                //Boolean isDeleted, Integer premiumTerm) {
                ProductEntity wholeLifeEntity02 = new WholeLifeProductEntity(WholeLifeProductEnum.CRITICALILLNESS, "Alibaba Whole Life Product 02", 78, BigDecimal.valueOf(50000),
                        "This is an Whole Life Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.", false, 20, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfRiders08 = new ArrayList<>();
                List<PremiumEntity> listOfPremium08 = new ArrayList<>();
                List<FeatureEntity> listOfFeatures08 = new ArrayList<>();

                // BigDecimal riderPremiumValue, String riderDescription
                RiderEntity rider08 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Whole Life Product 02");
                listOfRiders08.add(rider08);

                //Description
                FeatureEntity feature08 = new FeatureEntity("This additional feature is for Whole Life Product 02");
                listOfFeatures08.add(feature08);

                listOfPremium08.add(new PremiumEntity(23, 23, BigDecimal.valueOf(996), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(24, 24, BigDecimal.valueOf(1991), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(25, 25, BigDecimal.valueOf(2987), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(26, 26, BigDecimal.valueOf(3982), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(27, 27, BigDecimal.valueOf(4978), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(28, 28, BigDecimal.valueOf(5973), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(29, 29, BigDecimal.valueOf(6969), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(30, 30, BigDecimal.valueOf(7964), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(31, 31, BigDecimal.valueOf(8960), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(32, 32, BigDecimal.valueOf(9955), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(33, 33, BigDecimal.valueOf(14933), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(34, 34, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(35, 35, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(36, 36, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(37, 37, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(38, 38, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(39, 39, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium08.add(new PremiumEntity(40, 40, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));

                wholeLifeEntity02 = productSessionBean.createProductListing(wholeLifeEntity02, alibabaCompany.getCompanyId(), listOfRiders08, listOfPremium08, listOfFeatures08);

//*****************************************************************************************************************************************************************************************************************************************************            
                
                
                //TermLifeProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, 
                //Boolean isDeleted, Integer premiumTerm) {
                ProductEntity wholeLifeEntity03 = new WholeLifeProductEntity(WholeLifeProductEnum.HOSPITAL, "Alibaba Whole Life Product 03", 78, BigDecimal.valueOf(50000),
                        "This is an Whole Life Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.", false, 20, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfRiders09 = new ArrayList<>();
                List<PremiumEntity> listOfPremium09 = new ArrayList<>();
                List<FeatureEntity> listOfFeatures09 = new ArrayList<>();

                // BigDecimal riderPremiumValue, String riderDescription
                RiderEntity rider09 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Whole Life Product 03");
                listOfRiders09.add(rider09);
                
                
                listOfPremium09.add(new PremiumEntity(23, 23, BigDecimal.valueOf(996), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(24, 24, BigDecimal.valueOf(1991), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(25, 25, BigDecimal.valueOf(2987), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(26, 26, BigDecimal.valueOf(3982), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(27, 27, BigDecimal.valueOf(4978), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(28, 28, BigDecimal.valueOf(5973), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(29, 29, BigDecimal.valueOf(6969), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(30, 30, BigDecimal.valueOf(7964), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(31, 31, BigDecimal.valueOf(8960), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(32, 32, BigDecimal.valueOf(9955), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(33, 33, BigDecimal.valueOf(14933), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(34, 34, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(35, 35, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(36, 36, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(37, 37, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(38, 38, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(39, 39, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium09.add(new PremiumEntity(40, 40, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                
                wholeLifeEntity03 = productSessionBean.createProductListing(wholeLifeEntity03, alibabaCompany.getCompanyId(), listOfRiders09, listOfPremium09, listOfFeatures09);
                
//*****************************************************************************************************************************************************************************************************************************************************                            
                
                //TermLifeProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, 
                //Boolean isDeleted, Integer premiumTerm) {
                ProductEntity wholeLifeEntity04 = new WholeLifeProductEntity(WholeLifeProductEnum.LIFEINSURANCE, "Alibaba Whole Life Product 04", 78, BigDecimal.valueOf(50000),
                        "This is an Whole Life Product by Alibaba.\n We make the money work for you, with a 100% Guaranteed Capital, whilst providing you with insurance protection.", false, 20, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfRiders10 = new ArrayList<>();
                List<PremiumEntity> listOfPremium10 = new ArrayList<>();
                List<FeatureEntity> listOfFeatures10 = new ArrayList<>();
                
                
                listOfPremium10.add(new PremiumEntity(23, 23, BigDecimal.valueOf(996), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(24, 24, BigDecimal.valueOf(1991), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(25, 25, BigDecimal.valueOf(2987), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(26, 26, BigDecimal.valueOf(3982), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(27, 27, BigDecimal.valueOf(4978), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(28, 28, BigDecimal.valueOf(5973), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(29, 29, BigDecimal.valueOf(6969), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(30, 30, BigDecimal.valueOf(7964), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(31, 31, BigDecimal.valueOf(8960), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(32, 32, BigDecimal.valueOf(9955), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(33, 33, BigDecimal.valueOf(14933), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(34, 34, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(35, 35, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(36, 36, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(37, 37, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(38, 38, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(39, 39, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                listOfPremium10.add(new PremiumEntity(40, 40, BigDecimal.valueOf(19910), false, BigDecimal.valueOf(50000)));
                
                
                wholeLifeEntity04 = productSessionBean.createProductListing(wholeLifeEntity04, alibabaCompany.getCompanyId(), listOfRiders10, listOfPremium10, listOfFeatures10);
                
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                /*
                 * For Company 2
                 */
                //EndowmentProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, Boolean isDeleted, Integer premiumTerm) {
                ProductEntity coy2EndowmentEntity01 = new EndowmentEntity(EndowmentProductEnum.ENDOWMENT, "Tencent Endowment Product 01", 103, BigDecimal.valueOf(100000.00), "This is an Endowment Product by Tencent.\nWe make the money work for you, with a 100% Guaranteed Capital, "
                        + "whilst providing you with insurance protection.\n"
                        + "This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfcoy2Riders01 = new ArrayList<>();
                List<PremiumEntity> listOfcoy2Premium01 = new ArrayList<>();
                List<FeatureEntity> listOfcoy2Features01 = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity coy2rider01 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Tencent Endowment Product 01");
                listOfcoy2Riders01.add(coy2rider01);

                //Description
                FeatureEntity coy2feature01 = new FeatureEntity("This additional feature is for Tencent Endowment Product 01");
                listOfcoy2Features01.add(coy2feature01);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                listOfcoy2Premium01.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520)));
                listOfcoy2Premium01.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.valueOf(5040)));
                listOfcoy2Premium01.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.valueOf(7560)));
                listOfcoy2Premium01.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.valueOf(10080)));
                listOfcoy2Premium01.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.valueOf(12600)));
                listOfcoy2Premium01.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.valueOf(15120)));
                listOfcoy2Premium01.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.valueOf(17640)));
                listOfcoy2Premium01.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.valueOf(20160)));
                listOfcoy2Premium01.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.valueOf(22680)));
                listOfcoy2Premium01.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.valueOf(25200)));
                listOfcoy2Premium01.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.valueOf(27720)));
                listOfcoy2Premium01.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.valueOf(30240)));
                listOfcoy2Premium01.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.valueOf(32760)));
                listOfcoy2Premium01.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.valueOf(35280)));
                listOfcoy2Premium01.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.valueOf(37800)));
                listOfcoy2Premium01.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.valueOf(40320)));
                listOfcoy2Premium01.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.valueOf(42840)));
                listOfcoy2Premium01.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.valueOf(45360)));

                coy2EndowmentEntity01 = productSessionBean.createProductListing(coy2EndowmentEntity01, tencentCompany.getCompanyId(), listOfcoy2Riders01, listOfcoy2Premium01, listOfcoy2Features01);

                //---------------------------------
                ProductEntity coy2EndowmentEntity02 = new EndowmentEntity(EndowmentProductEnum.ENDOWMENT, "Tencent Endowment Product 02", 103, BigDecimal.valueOf(100000.00), "This is an Endowment Product by Tencent.\nWe make the money work for you, with a 100% Guaranteed Capital, "
                        + "whilst providing you with insurance protection.\n"
                        + "This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfcoy2Riders02 = new ArrayList<>();
                List<PremiumEntity> listOfcoy2Premium02 = new ArrayList<>();
                List<FeatureEntity> listOfcoy2Features02 = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity coy2rider02 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Tencent Endowment Product 02");
                listOfcoy2Riders02.add(coy2rider02);

                //Description
                FeatureEntity coy2feature02 = new FeatureEntity("This additional feature is for Tencent Endowment Product 02");
                listOfcoy2Features02.add(coy2feature02);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                listOfcoy2Premium02.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520)));
                listOfcoy2Premium02.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.valueOf(5040)));
                listOfcoy2Premium02.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.valueOf(7560)));
                listOfcoy2Premium02.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.valueOf(10080)));
                listOfcoy2Premium02.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.valueOf(12600)));
                listOfcoy2Premium02.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.valueOf(15120)));
                listOfcoy2Premium02.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.valueOf(17640)));
                listOfcoy2Premium02.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.valueOf(20160)));
                listOfcoy2Premium02.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.valueOf(22680)));
                listOfcoy2Premium02.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.valueOf(25200)));
                listOfcoy2Premium02.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.valueOf(27720)));
                listOfcoy2Premium02.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.valueOf(30240)));
                listOfcoy2Premium02.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.valueOf(32760)));
                listOfcoy2Premium02.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.valueOf(35280)));
                listOfcoy2Premium02.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.valueOf(37800)));
                listOfcoy2Premium02.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.valueOf(40320)));
                listOfcoy2Premium02.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.valueOf(42840)));
                listOfcoy2Premium02.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.valueOf(45360)));

                coy2EndowmentEntity02 = productSessionBean.createProductListing(coy2EndowmentEntity02, tencentCompany.getCompanyId(), listOfcoy2Riders02, listOfcoy2Premium02, listOfcoy2Features02);

                // ----------------
                ProductEntity coy2EndowmentEntity03 = new EndowmentEntity(EndowmentProductEnum.ENDOWMENT, "Tencent Endowment Product 03", 103, BigDecimal.valueOf(100000.00), "This is an Endowment Product by Tencent.\nWe make the money work for you, with a 100% Guaranteed Capital, "
                        + "whilst providing you with insurance protection.\n"
                        + "This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfcoy2Riders03 = new ArrayList<>();
                List<PremiumEntity> listOfcoy2Premium03 = new ArrayList<>();
                List<FeatureEntity> listOfcoy2Features03 = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity coy2rider03 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Tencent Endowment Product 03");
                listOfcoy2Riders03.add(coy2rider03);

                //Description
                FeatureEntity coy2feature03 = new FeatureEntity("This additional feature is for Tencent Endowment Product 03");
                listOfcoy2Features03.add(coy2feature03);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                listOfcoy2Premium03.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520)));
                listOfcoy2Premium03.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.valueOf(5040)));
                listOfcoy2Premium03.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.valueOf(7560)));
                listOfcoy2Premium03.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.valueOf(10080)));
                listOfcoy2Premium03.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.valueOf(12600)));
                listOfcoy2Premium03.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.valueOf(15120)));
                listOfcoy2Premium03.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.valueOf(17640)));
                listOfcoy2Premium03.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.valueOf(20160)));
                listOfcoy2Premium03.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.valueOf(22680)));
                listOfcoy2Premium03.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.valueOf(25200)));
                listOfcoy2Premium03.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.valueOf(27720)));
                listOfcoy2Premium03.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.valueOf(30240)));
                listOfcoy2Premium03.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.valueOf(32760)));
                listOfcoy2Premium03.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.valueOf(35280)));
                listOfcoy2Premium03.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.valueOf(37800)));
                listOfcoy2Premium03.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.valueOf(40320)));
                listOfcoy2Premium03.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.valueOf(42840)));
                listOfcoy2Premium03.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.valueOf(45360)));

                coy2EndowmentEntity03 = productSessionBean.createProductListing(coy2EndowmentEntity03, tencentCompany.getCompanyId(), listOfcoy2Riders03, listOfcoy2Premium03, listOfcoy2Features03);

                // WholeLife 
                ProductEntity coy2WholeLifeProduct01 = new WholeLifeProductEntity(WholeLifeProductEnum.LIFEINSURANCE, "Tencent Whole Life Insurance Product 01", 103, BigDecimal.valueOf(100000.00), "This is an WholeLife Product by Tencent.\nWe make the money work for you, with a 100% Guaranteed Capital, "
                        + "whilst providing you with insurance protection.\n"
                        + "This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfcoy2Riders04 = new ArrayList<>();
                List<PremiumEntity> listOfcoy2Premium04 = new ArrayList<>();
                List<FeatureEntity> listOfcoy2Features04 = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity coy2rider04 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Tencent Whole Life Insurance Product 01");
                listOfcoy2Riders04.add(coy2rider04);

                //Description
                FeatureEntity coy2feature04 = new FeatureEntity("This additional feature is for Tencent Whole Life Insurance Product 01");
                listOfcoy2Features04.add(coy2feature04);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                listOfcoy2Premium04.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520)));
                listOfcoy2Premium04.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.valueOf(5040)));
                listOfcoy2Premium04.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.valueOf(7560)));
                listOfcoy2Premium04.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.valueOf(10080)));
                listOfcoy2Premium04.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.valueOf(12600)));
                listOfcoy2Premium04.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.valueOf(15120)));
                listOfcoy2Premium04.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.valueOf(17640)));
                listOfcoy2Premium04.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.valueOf(20160)));
                listOfcoy2Premium04.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.valueOf(22680)));
                listOfcoy2Premium04.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.valueOf(25200)));
                listOfcoy2Premium04.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.valueOf(27720)));
                listOfcoy2Premium04.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.valueOf(30240)));
                listOfcoy2Premium04.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.valueOf(32760)));
                listOfcoy2Premium04.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.valueOf(35280)));
                listOfcoy2Premium04.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.valueOf(37800)));
                listOfcoy2Premium04.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.valueOf(40320)));
                listOfcoy2Premium04.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.valueOf(42840)));
                listOfcoy2Premium04.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.valueOf(45360)));

                coy2WholeLifeProduct01 = productSessionBean.createProductListing(coy2WholeLifeProduct01, tencentCompany.getCompanyId(), listOfcoy2Riders04, listOfcoy2Premium04, listOfcoy2Features04);

                //----------------------------
                ProductEntity coy2WholeLifeProduct02 = new WholeLifeProductEntity(WholeLifeProductEnum.HOSPITAL, "Tencent Whole Life Insurance Product 02", 103, BigDecimal.valueOf(100000.00), "This is an WholeLife Product by Tencent.\nWe make the money work for you, with a 100% Guaranteed Capital, "
                        + "whilst providing you with insurance protection.\n"
                        + "This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfcoy2Riders05 = new ArrayList<>();
                List<PremiumEntity> listOfcoy2Premium05 = new ArrayList<>();
                List<FeatureEntity> listOfcoy2Features05 = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity coy2rider05 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Tencent Whole Life Insurance Product 02");
                listOfcoy2Riders05.add(coy2rider05);

                //Description
                FeatureEntity coy2feature05 = new FeatureEntity("This additional feature is for Tencent Whole Life Insurance Product 02");
                listOfcoy2Features05.add(coy2feature05);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                listOfcoy2Premium05.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520)));
                listOfcoy2Premium05.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.valueOf(5040)));
                listOfcoy2Premium05.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.valueOf(7560)));
                listOfcoy2Premium05.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.valueOf(10080)));
                listOfcoy2Premium05.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.valueOf(12600)));
                listOfcoy2Premium05.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.valueOf(15120)));
                listOfcoy2Premium05.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.valueOf(17640)));
                listOfcoy2Premium05.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.valueOf(20160)));
                listOfcoy2Premium05.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.valueOf(22680)));
                listOfcoy2Premium05.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.valueOf(25200)));
                listOfcoy2Premium05.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.valueOf(27720)));
                listOfcoy2Premium05.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.valueOf(30240)));
                listOfcoy2Premium05.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.valueOf(32760)));
                listOfcoy2Premium05.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.valueOf(35280)));
                listOfcoy2Premium05.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.valueOf(37800)));
                listOfcoy2Premium05.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.valueOf(40320)));
                listOfcoy2Premium05.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.valueOf(42840)));
                listOfcoy2Premium05.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.valueOf(45360)));

                coy2WholeLifeProduct02 = productSessionBean.createProductListing(coy2WholeLifeProduct02, tencentCompany.getCompanyId(), listOfcoy2Riders05, listOfcoy2Premium05, listOfcoy2Features05);

                //---------------------
                ProductEntity coy2WholeLifeProduct03 = new WholeLifeProductEntity(WholeLifeProductEnum.CRITICALILLNESS, "Tencent Whole Life Insurance Product 03", 103, BigDecimal.valueOf(100000.00), "This is an WholeLife Product by Tencent.\nWe make the money work for you, with a 100% Guaranteed Capital, "
                        + "whilst providing you with insurance protection.\n"
                        + "This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfcoy2Riders06 = new ArrayList<>();
                List<PremiumEntity> listOfcoy2Premium06 = new ArrayList<>();
                List<FeatureEntity> listOfcoy2Features06 = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity coy2rider06 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Tencent Whole Life Insurance Product 03");
                listOfcoy2Riders06.add(coy2rider06);

                //Description
                FeatureEntity coy2feature06 = new FeatureEntity("This additional feature is for Tencent Whole Life Insurance Product 03");
                listOfcoy2Features06.add(coy2feature06);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                listOfcoy2Premium06.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520)));
                listOfcoy2Premium06.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.valueOf(5040)));
                listOfcoy2Premium06.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.valueOf(7560)));
                listOfcoy2Premium06.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.valueOf(10080)));
                listOfcoy2Premium06.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.valueOf(12600)));
                listOfcoy2Premium06.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.valueOf(15120)));
                listOfcoy2Premium06.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.valueOf(17640)));
                listOfcoy2Premium06.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.valueOf(20160)));
                listOfcoy2Premium06.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.valueOf(22680)));
                listOfcoy2Premium06.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.valueOf(25200)));
                listOfcoy2Premium06.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.valueOf(27720)));
                listOfcoy2Premium06.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.valueOf(30240)));
                listOfcoy2Premium06.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.valueOf(32760)));
                listOfcoy2Premium06.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.valueOf(35280)));
                listOfcoy2Premium06.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.valueOf(37800)));
                listOfcoy2Premium06.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.valueOf(40320)));
                listOfcoy2Premium06.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.valueOf(42840)));
                listOfcoy2Premium06.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.valueOf(45360)));

                coy2WholeLifeProduct03 = productSessionBean.createProductListing(coy2WholeLifeProduct03, tencentCompany.getCompanyId(), listOfcoy2Riders06, listOfcoy2Premium06, listOfcoy2Features06);

                // --------------------------
                ProductEntity coy2WholeLifeProduct04 = new WholeLifeProductEntity(WholeLifeProductEnum.ACCIDENT, "Tencent Whole Life Insurance Product 04", 103, BigDecimal.valueOf(100000.00), "This is an WholeLife Product by Tencent.\nWe make the money work for you, with a 100% Guaranteed Capital, "
                        + "whilst providing you with insurance protection.\n"
                        + "This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfcoy2Riders07 = new ArrayList<>();
                List<PremiumEntity> listOfcoy2Premium07 = new ArrayList<>();
                List<FeatureEntity> listOfcoy2Features07 = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity coy2rider07 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Tencent Whole Life Insurance Product 04");
                listOfcoy2Riders07.add(coy2rider07);

                //Description
                FeatureEntity coy2feature07 = new FeatureEntity("This additional feature is for Tencent Whole Life Insurance Product 04");
                listOfcoy2Features07.add(coy2feature07);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                listOfcoy2Premium07.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.valueOf(2520)));
                listOfcoy2Premium07.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.valueOf(5040)));
                listOfcoy2Premium07.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.valueOf(7560)));
                listOfcoy2Premium07.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.valueOf(10080)));
                listOfcoy2Premium07.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.valueOf(12600)));
                listOfcoy2Premium07.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.valueOf(15120)));
                listOfcoy2Premium07.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.valueOf(17640)));
                listOfcoy2Premium07.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.valueOf(20160)));
                listOfcoy2Premium07.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.valueOf(22680)));
                listOfcoy2Premium07.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.valueOf(25200)));
                listOfcoy2Premium07.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.valueOf(27720)));
                listOfcoy2Premium07.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.valueOf(30240)));
                listOfcoy2Premium07.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.valueOf(32760)));
                listOfcoy2Premium07.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.valueOf(35280)));
                listOfcoy2Premium07.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.valueOf(37800)));
                listOfcoy2Premium07.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.valueOf(40320)));
                listOfcoy2Premium07.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.valueOf(42840)));
                listOfcoy2Premium07.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.valueOf(45360)));

                coy2WholeLifeProduct04 = productSessionBean.createProductListing(coy2WholeLifeProduct04, tencentCompany.getCompanyId(), listOfcoy2Riders07, listOfcoy2Premium07, listOfcoy2Features07);

                // --------------------------
                // Term Life
                ProductEntity coy2TermLifeProduct01 = new TermLifeProductEntity(TermLifeProductEnum.ACCIDENT, "Tencent Term Life Insurance Product 01", 103, BigDecimal.valueOf(100000.00), "This is an WholeLife Product by Tencent.\nWe make the money work for you, with a 100% Guaranteed Capital, "
                        + "whilst providing you with insurance protection.\n"
                        + "This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfcoy2Riders08 = new ArrayList<>();
                List<PremiumEntity> listOfcoy2Premium08 = new ArrayList<>();
                List<FeatureEntity> listOfcoy2Features08 = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity coy2rider08 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Tencent Whole Life Insurance Product 01");
                listOfcoy2Riders08.add(coy2rider08);

                //Description
                FeatureEntity coy2feature08 = new FeatureEntity("This additional feature is for Tencent Whole Life Insurance Product 01");
                listOfcoy2Features08.add(coy2feature08);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                listOfcoy2Premium08.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.ZERO));
                listOfcoy2Premium08.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.ZERO));

                coy2TermLifeProduct01 = productSessionBean.createProductListing(coy2TermLifeProduct01, tencentCompany.getCompanyId(), listOfcoy2Riders08, listOfcoy2Premium08, listOfcoy2Features08);

                // --------------------------
                ProductEntity coy2TermLifeProduct02 = new TermLifeProductEntity(TermLifeProductEnum.CRITICALILLNESS, "Tencent Term Life Insurance Product 02", 103, BigDecimal.valueOf(100000.00), "This is an WholeLife Product by Tencent.\nWe make the money work for you, with a 100% Guaranteed Capital, "
                        + "whilst providing you with insurance protection.\n"
                        + "This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfcoy2Riders09 = new ArrayList<>();
                List<PremiumEntity> listOfcoy2Premium09 = new ArrayList<>();
                List<FeatureEntity> listOfcoy2Features09 = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity coy2rider09 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Tencent Whole Life Insurance Product 02");
                listOfcoy2Riders09.add(coy2rider09);

                //Description
                FeatureEntity coy2feature09 = new FeatureEntity("This additional feature is for Tencent Whole Life Insurance Product 02");
                listOfcoy2Features09.add(coy2feature09);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                listOfcoy2Premium09.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.ZERO));
                listOfcoy2Premium09.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.ZERO));

                coy2TermLifeProduct02 = productSessionBean.createProductListing(coy2TermLifeProduct02, tencentCompany.getCompanyId(), listOfcoy2Riders09, listOfcoy2Premium09, listOfcoy2Features09);

                //-----------------------
                ProductEntity coy2TermLifeProduct03 = new TermLifeProductEntity(TermLifeProductEnum.HOSPITAL, "Tencent Term Life Insurance Product 03", 103, BigDecimal.valueOf(100000.00), "This is an WholeLife Product by Tencent.\nWe make the money work for you, with a 100% Guaranteed Capital, "
                        + "whilst providing you with insurance protection.\n"
                        + "This comes with the option for Policy Continuity, allowing your spouse or child (below 16) as the secondary insured, "
                        + "allowing the endowment plan to carry forward in the evnet of unfortunate circumstances", false, 100, PolicyCurrencyEnum.SGD);

                List<RiderEntity> listOfcoy2Riders10 = new ArrayList<>();
                List<PremiumEntity> listOfcoy2Premium10 = new ArrayList<>();
                List<FeatureEntity> listOfcoy2Features10 = new ArrayList<>();

//              BigDecimal riderPremiumValue, String riderDescription
                RiderEntity coy2rider10 = new RiderEntity(BigDecimal.valueOf(1200), "This rider is for Tencent Whole Life Insurance Product 03");
                listOfcoy2Riders10.add(coy2rider10);

                //Description
                FeatureEntity coy2feature10 = new FeatureEntity("This additional feature is for Tencent Whole Life Insurance Product 03");
                listOfcoy2Features10.add(coy2feature10);

                //Integer minAgeGroup, Integer maxAgeGroup, BigDecimal value, Boolean isSmoker, BigDecimal guaranteeSum
                listOfcoy2Premium10.add(new PremiumEntity(23, 23, BigDecimal.valueOf(2400), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(24, 24, BigDecimal.valueOf(4800), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(25, 25, BigDecimal.valueOf(7200), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(26, 26, BigDecimal.valueOf(9600), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(27, 27, BigDecimal.valueOf(12000), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(28, 28, BigDecimal.valueOf(14400), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(29, 29, BigDecimal.valueOf(16800), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(30, 30, BigDecimal.valueOf(19200), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(31, 31, BigDecimal.valueOf(21600), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(32, 32, BigDecimal.valueOf(24000), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(33, 33, BigDecimal.valueOf(26400), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(34, 34, BigDecimal.valueOf(28800), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(35, 35, BigDecimal.valueOf(31200), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(36, 36, BigDecimal.valueOf(33600), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(37, 37, BigDecimal.valueOf(36000), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(38, 38, BigDecimal.valueOf(38400), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(39, 39, BigDecimal.valueOf(40800), false, BigDecimal.ZERO));
                listOfcoy2Premium10.add(new PremiumEntity(40, 40, BigDecimal.valueOf(43200), false, BigDecimal.ZERO));

                coy2TermLifeProduct03 = productSessionBean.createProductListing(coy2TermLifeProduct03, tencentCompany.getCompanyId(), listOfcoy2Riders10, listOfcoy2Premium10, listOfcoy2Features10);

            } catch (CompanyAlreadyExistException | UnknownPersistenceException | CompanyCreationException | PointOfContactAlreadyExistsException
                    | InvalidPointOfContactCreationException | CustomerAlreadyExistException | CustomerCreationException | ProductAlreadyExistsException | InvalidProductCreationException ex) {
                System.out.println(ex.getMessage());

            }
        }

    }

}
