/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.entity.CompanyEntity;
import ejb.entity.CreditPaymentEntity;
import ejb.entity.EndowmentEntity;
import ejb.entity.FeatureEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.PointOfContactEntity;
import ejb.entity.PremiumEntity;
import ejb.entity.ProductEntity;
import ejb.entity.ProductLineItemEntity;
import ejb.entity.RiderEntity;
import ejb.entity.TermLifeProductEntity;
import ejb.entity.WholeLifeProductEntity;
import ejb.stateless.CompanySessionBeanLocal;
import ejb.stateless.ProductSessionBeanLocal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CompanyBeanValidaionException;
import util.exception.CompanyDoesNotExistException;
import util.exception.CompanySQLConstraintException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.InvalidProductCreationException;
import util.exception.PointOfContactBeanValidationException;
import util.exception.ProductAlreadyExistsException;
import util.exception.UnknownPersistenceException;
import ws.datamodel.ProductEntityWrapper;

/**
 * REST Web Service
 *
 * @author sohqi
 */
@Path("Product")
public class ProductResource {

    CompanySessionBeanLocal companySessionBeanLocal = lookupCompanySessionBeanLocal();

    ProductSessionBeanLocal productSessionBeanLocal = lookupProductSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ProductResource
     */
    public ProductResource() {
    }

    /**
     * working
     *
     * @return
     */
    @Path("retrieveAllFinancialProducts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllFinancialProducts() {
        try {
            List<ProductEntity> products = productSessionBeanLocal.retrieveAllFinancialProducts();
            for (ProductEntity product : products) {

                product = nullifyProduct(product);
            }
            GenericEntity<List<ProductEntity>> genericEntity = new GenericEntity<List<ProductEntity>>(products) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("***********" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     *
     * working
     *
     * @return
     */
    @Path("retrieveAllEndowmentProducts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllEndowmentProducts() {
        try {
            List<EndowmentEntity> products = productSessionBeanLocal.retrieveAllEndowmentProducts();
            for (ProductEntity product : products) {
                product = nullifyProduct(product);
//                if (product.getCompany() != null) {
//                    product.getCompany().setListOfProducts(null);
//                    if (product.getCompany().getRefund() != null) {
//                        product.getCompany().getRefund().setCompany(null);
//                    }
//                    if (product.getCompany().getListOfPayments() != null && !product.getCompany().getListOfPayments().isEmpty()) {
//                        for (PaymentEntity pay : product.getCompany().getListOfPayments()) {
//                            pay.setCompany(null);
//                            if (pay instanceof MonthlyPaymentEntity) {
//                                MonthlyPaymentEntity c = ((MonthlyPaymentEntity) pay);
//                                for (ProductLineItemEntity pl : c.getListOfProductLineItems()) {
//                                    if (pl.getProduct() != null) {
//                                        pl.setProduct(null);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (product.getCompany().getListOfPointOfContacts() != null && !product.getCompany().getListOfPointOfContacts().isEmpty()) {
//                        for (PointOfContactEntity poc : product.getCompany().getListOfPointOfContacts()) {
//                            poc.setCompany(null);
//
//                        }
//                    }
//
//                }
            }
            GenericEntity<List<EndowmentEntity>> genericEntity = new GenericEntity<List<EndowmentEntity>>(products) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("***********" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * working
     *
     * @return
     */
    @Path("retrieveAllTermLifeProducts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTermLifeProducts() {
        try {
            List<TermLifeProductEntity> products = productSessionBeanLocal.retrieveAllTermLifeProducts();
            for (ProductEntity product : products) {

                product = nullifyProduct(product);
            }
            GenericEntity<List<TermLifeProductEntity>> genericEntity = new GenericEntity<List<TermLifeProductEntity>>(products) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("***********" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * working
     *
     * @return
     */
    @Path("retrieveAllWholeLifeProducts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllWholeLifeProducts() {
        try {
            List<WholeLifeProductEntity> products = productSessionBeanLocal.retrieveAllWholeLifeProducts();
            for (ProductEntity product : products) {

                product = nullifyProduct(product);
            }
            GenericEntity<List<WholeLifeProductEntity>> genericEntity = new GenericEntity<List<WholeLifeProductEntity>>(products) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("***********" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * working
     *
     * @param productId
     * @return
     */
    @Path("retrieveProductEntityById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveProductEntityById(@QueryParam("productId") Long productId) {
        try {
            ProductEntity product = productSessionBeanLocal.retrieveProductEntityById(productId);

            product = nullifyProduct(product);
            GenericEntity<ProductEntity> genericEntity = new GenericEntity<ProductEntity>(product) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("***********" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * working
     *
     * @param productId
     * @return
     */
    @Path("retrieveProductEntityWrapperById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveProductEntityWrapperById(@QueryParam("productId") Long productId) {
        try {
            ProductEntity product = productSessionBeanLocal.retrieveProductEntityById(productId);

            product = nullifyProduct(product);
            ProductEntityWrapper productEntityWrapper = new ProductEntityWrapper();
            productEntityWrapper.setProduct(product);
            if (product instanceof EndowmentEntity) {
                productEntityWrapper.setProductType("ENDOWMENT");
                productEntityWrapper.setProductEnum(((EndowmentEntity) product).getProductEnum().toString());
            } else if (product instanceof TermLifeProductEntity) {
                productEntityWrapper.setProductType("TERMLIFEPRODUCT");
                productEntityWrapper.setProductEnum(((TermLifeProductEntity) product).getProductEnum().toString());
            } else if (product instanceof WholeLifeProductEntity) {
                productEntityWrapper.setProductType("WHOLELIFEPRODUCT");
                productEntityWrapper.setProductEnum(((WholeLifeProductEntity) product).getProductEnum().toString());
            }
            if (product.getIsAvailableToSmokers()) {
                productEntityWrapper.setIsSmoker("true");
            } else if (!product.getIsAvailableToSmokers()) {
                productEntityWrapper.setIsSmoker("false");
            }

            GenericEntity<ProductEntityWrapper> genericEntity = new GenericEntity<ProductEntityWrapper>(productEntityWrapper) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("***********" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * working
     *
     * @param name
     * @return
     */
    @Path("searchForProductsByName")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchForProductsByName(@QueryParam("name") String name) {
        try {
            List<ProductEntity> products = productSessionBeanLocal.searchForProductsByName(name);
            for (ProductEntity product : products) {

                product = nullifyProduct(product);
            }
            GenericEntity<List<ProductEntity>> genericEntity = new GenericEntity<List<ProductEntity>>(products) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("***********" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * working
     *
     * @param name
     * @return
     */
    @Path("retrieveListOfProductByCompany")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveListOfProductByCompany(@QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            CompanyEntity company = companySessionBeanLocal.login(email, password);
            if (company == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid account").build();
            }
            List<ProductEntity> products = productSessionBeanLocal.retrieveListOfProductByCompany(company.getCompanyEmail());
            for (ProductEntity product : products) {

                product = nullifyProduct(product);
            }
            GenericEntity<List<ProductEntity>> genericEntity = new GenericEntity<List<ProductEntity>>(products) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (CompanyDoesNotExistException | IncorrectLoginParticularsException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid account").build();
        } catch (Exception ex) {
            System.out.println("***********" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * not tested
     *
     * @param productWrapper
     * @return
     */
    @Path("deleteProductListing")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProductListing(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("productId") Long productId) {
        try {
            CompanyEntity company = companySessionBeanLocal.login(email, password);
            if (company == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid account").build();
            }
            productSessionBeanLocal.deleteProductListing(productId);

            GenericEntity<String> genericEntity = new GenericEntity<String>("") {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (CompanyDoesNotExistException | IncorrectLoginParticularsException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid account").build();
        } catch (Exception ex) {
            System.out.println("***********" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * working
     *
     * @param email
     * @param password
     * @param newRecord
     * @return
     */
    @Path("Endownment")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewRecordForEndowment(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("isSmoker") boolean isSmoker, EndowmentEntity newRecord) {
        System.out.println("create new record **** *");
        if (newRecord != null) {
            try {
                CompanyEntity company = companySessionBeanLocal.login(email, password);
                if (company != null) {

                    EndowmentEntity product = newRecord;
                    System.out.println("Check smoker for record: " + isSmoker);
                    product.setIsAvailableToSmokers(isSmoker);

                    List<RiderEntity> listOfRiders = newRecord.getListOfRiders();
                    List<PremiumEntity> listOfPremium = newRecord.getListOfPremium();
                    List<PremiumEntity> listOfSmoker = newRecord.getListOfSmokerPremium();
                    List<FeatureEntity> listOfFeatures = newRecord.getListOfAdditionalFeatures();

                    //cut of tie
                    product.setListOfAdditionalFeatures(new ArrayList<FeatureEntity>());
                    product.setListOfPremium(new ArrayList<PremiumEntity>());
                    product.setListOfSmokerPremium(new ArrayList<PremiumEntity>());
                    product.setListOfRiders(new ArrayList<RiderEntity>());
                    product.getCompany().setListOfProducts(null);
                    product.setCompany(null);

                    ProductEntity returnProduct = productSessionBeanLocal.createProductListing(product, company.getCompanyId(), listOfRiders,
                            listOfPremium, listOfSmoker, listOfFeatures);
                    returnProduct = nullifyProduct(returnProduct);
                    return Response.status(Response.Status.OK).entity(returnProduct).build();
                } else {

                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
                }

            } catch (Exception ex) {
                System.out.println("***********ex.message" + ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }

    /**
     * working
     *
     * @param email
     * @param password
     * @param newRecord
     * @param isSmoker
     * @return
     */
    @Path("TermLifeProductEntity")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewRecordForTermLifeProductEntity(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("isSmoker") boolean isSmoker, TermLifeProductEntity newRecord) {
        System.out.println("create new record **** *");
        if (newRecord != null) {
            try {
                CompanyEntity company = companySessionBeanLocal.login(email, password);
                if (company != null) {
                    TermLifeProductEntity product = newRecord;
                    System.out.println("Check smoker for record: " + isSmoker);
                    product.setIsAvailableToSmokers(isSmoker);
                    List<RiderEntity> listOfRiders = newRecord.getListOfRiders();
                    List<PremiumEntity> listOfPremium = newRecord.getListOfPremium();
                    List<PremiumEntity> listOfSmoker = newRecord.getListOfSmokerPremium();
                    List<FeatureEntity> listOfFeatures = newRecord.getListOfAdditionalFeatures();

                    //cut of tie
                    product.setListOfAdditionalFeatures(new ArrayList<FeatureEntity>());
                    product.setListOfPremium(new ArrayList<PremiumEntity>());
                    product.setListOfSmokerPremium(new ArrayList<PremiumEntity>());
                    product.setListOfRiders(new ArrayList<RiderEntity>());
                    product.getCompany().setListOfProducts(null);
                    product.setCompany(null);

                    ProductEntity returnProduct = productSessionBeanLocal.createProductListing(product, company.getCompanyId(), listOfRiders,
                            listOfPremium, listOfSmoker, listOfFeatures);
                    returnProduct = nullifyProduct(returnProduct);
                    return Response.status(Response.Status.OK).entity(returnProduct).build();
                } else {

                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
                }

            } catch (Exception ex) {
                System.out.println("***********ex.message" + ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }

    /**
     * working
     *
     * @param email
     * @param password
     * @param newRecord
     * @return
     */
    @Path("WholeLifeProductEntity")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewRecordForWholeLifeProductEntity(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("isSmoker") boolean isSmoker, WholeLifeProductEntity newRecord) {
        if (newRecord != null) {
            try {
                CompanyEntity company = companySessionBeanLocal.login(email, password);
                if (company != null) {

                    WholeLifeProductEntity wholeLifeProductEntity = newRecord;
                    System.out.println("Check smoker for record: " + isSmoker);
                    wholeLifeProductEntity.setIsAvailableToSmokers(isSmoker);

                    List<RiderEntity> listOfRiders = newRecord.getListOfRiders();
                    List<PremiumEntity> listOfPremium = newRecord.getListOfPremium();
                    List<PremiumEntity> listOfSmoker = newRecord.getListOfSmokerPremium();
                    List<FeatureEntity> listOfFeatures = newRecord.getListOfAdditionalFeatures();
                    newRecord.getCompany().setListOfProducts(null);
                    //cut of tie
                    wholeLifeProductEntity.setListOfAdditionalFeatures(new ArrayList<FeatureEntity>());
                    wholeLifeProductEntity.setListOfPremium(new ArrayList<PremiumEntity>());
                    wholeLifeProductEntity.setListOfSmokerPremium(new ArrayList<PremiumEntity>());
                    wholeLifeProductEntity.setListOfRiders(new ArrayList<RiderEntity>());
                    wholeLifeProductEntity.getCompany().setListOfProducts(null);
                    wholeLifeProductEntity.setCompany(null);
                    ProductEntity returnProduct = productSessionBeanLocal.createProductListing(wholeLifeProductEntity, company.getCompanyId(), listOfRiders,
                            listOfPremium, listOfSmoker, listOfFeatures);
                    returnProduct = nullifyProduct(returnProduct);
                    return Response.status(Response.Status.OK).entity(returnProduct).build();
                } else {

                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
                }

            } catch (Exception ex) {
                System.out.println("***********ex.message" + ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }

    @POST
    @Path("updateProductInformation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductInformation(ProductEntity product, @QueryParam("email") String email, @QueryParam("password") String password) {
        if (product != null) {
            try {

                CompanyEntity tempCompanyEntity = companySessionBeanLocal.login(email, password);
                ProductEntity prod = productSessionBeanLocal.updateProductListingWS(product);
                prod = nullifyProduct(prod);

                return Response.status(Response.Status.OK).entity(prod).build();
            } catch (CompanyDoesNotExistException | IncorrectLoginParticularsException ex) {
//                System.out.println("ex.message" + ex.getMessage());
                System.out.println("ex.message incorrect login" + ex.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (UnknownPersistenceException | ProductAlreadyExistsException | InvalidProductCreationException exception) {
                System.out.println("ex.message unknown" + exception.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).build();
            } catch (Exception ex) {
                System.out.println("ex.message" + ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }

    }

    @POST
    @Path("updateProductInformationTermLife")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductInformationTermLife(TermLifeProductEntity product, @QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("isSmoker") boolean isSmoker) {
        if (product != null) {
            try {

                CompanyEntity tempCompanyEntity = companySessionBeanLocal.login(email, password);
                System.out.println("Check smoker for record: " + isSmoker);
                product.setIsAvailableToSmokers(isSmoker);
                ProductEntity prod = productSessionBeanLocal.updateProductListingWS(product);
                prod = nullifyProduct(prod);
                TermLifeProductEntity termlife = (TermLifeProductEntity) prod;
                ProductEntityWrapper pr = new ProductEntityWrapper();
                if (prod.getIsAvailableToSmokers()) {
                    pr.setIsSmoker("true");
                } else {
                    pr.setIsSmoker("false");
                }
                pr.setProductType("TERMLIFEPRODUCT");
                pr.setProduct(prod);
                pr.setProductEnum(termlife.getProductEnum().toString());
                return Response.status(Response.Status.OK).entity(pr).build();
            } catch (CompanyDoesNotExistException | IncorrectLoginParticularsException ex) {
//                System.out.println("ex.message" + ex.getMessage());
                System.out.println("ex.message incorrect login" + ex.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (UnknownPersistenceException | ProductAlreadyExistsException | InvalidProductCreationException exception) {
                System.out.println("ex.message unknown" + exception.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).build();
            } catch (Exception ex) {
                System.out.println("ex.message" + ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }

    }

    @POST
    @Path("updateProductInformationWholeLife")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductInformationWholeLife(WholeLifeProductEntity product, @QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("isSmoker") boolean isSmoker) {
        if (product != null) {
            try {

                CompanyEntity tempCompanyEntity = companySessionBeanLocal.login(email, password);
                System.out.println("Check smoker for record: " + isSmoker);
                product.setIsAvailableToSmokers(isSmoker);
                ProductEntity prod = productSessionBeanLocal.updateProductListingWS(product);
                prod = nullifyProduct(prod);
                WholeLifeProductEntity wholeLfe = (WholeLifeProductEntity) prod;
                ProductEntityWrapper pr = new ProductEntityWrapper();
                if (prod.getIsAvailableToSmokers()) {
                    pr.setIsSmoker("true");
                } else {
                    pr.setIsSmoker("false");
                }
                pr.setProductType("WHOLELIFEPRODUCT");
                pr.setProduct(prod);
                pr.setProductEnum(product.getProductEnum().toString());
                return Response.status(Response.Status.OK).entity(pr).build();
            } catch (CompanyDoesNotExistException | IncorrectLoginParticularsException ex) {
//                System.out.println("ex.message" + ex.getMessage());
                System.out.println("ex.message incorrect login" + ex.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (UnknownPersistenceException | ProductAlreadyExistsException | InvalidProductCreationException exception) {
                System.out.println("ex.message unknown" + exception.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).build();
            } catch (Exception ex) {
                System.out.println("ex.message" + ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }

    }

    @POST
    @Path("updateProductInformationEndowment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductInformationEndowment(EndowmentEntity product, @QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("isSmoker") boolean isSmoker) {
        if (product != null) {
            try {

                CompanyEntity tempCompanyEntity = companySessionBeanLocal.login(email, password);
                System.out.println("Check smoker for record: " + isSmoker);
                product.setIsAvailableToSmokers(isSmoker);
                ProductEntity prod = productSessionBeanLocal.updateProductListingWS(product);
                prod = nullifyProduct(prod);
                EndowmentEntity endowment = (EndowmentEntity) prod;
                ProductEntityWrapper pr = new ProductEntityWrapper();
                if (prod.getIsAvailableToSmokers()) {
                    pr.setIsSmoker("true");
                } else {
                    pr.setIsSmoker("false");
                }
                pr.setProductType("ENDOWMENT");
                pr.setProduct(prod);
                pr.setProductEnum(product.getProductEnum().toString());
                return Response.status(Response.Status.OK).entity(pr).build();
            } catch (CompanyDoesNotExistException | IncorrectLoginParticularsException ex) {
//                System.out.println("ex.message" + ex.getMessage());
                System.out.println("ex.message incorrect login" + ex.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (UnknownPersistenceException | ProductAlreadyExistsException | InvalidProductCreationException exception) {
                System.out.println("ex.message unknown" + exception.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).build();
            } catch (Exception ex) {
                System.out.println("ex.message" + ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }

    }

    @Path("filterProductByDateCreated")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSpecificMonthlyCreditHistoricalTransactions(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
        try {
            CompanyEntity company = companySessionBeanLocal.login(email, password);
            if (company == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid account").build();
            }
            String[] splitStartDate = startDate.split("-");
            String[] splitEndDate = endDate.split("-");
            Calendar dStartDate = new GregorianCalendar();
            dStartDate.set(Integer.parseInt(splitStartDate[0]), Integer.parseInt(splitStartDate[1]) - 1, Integer.parseInt(splitStartDate[2]), 0, 0);

            Calendar dEndDate = new GregorianCalendar();
            dEndDate.set(Integer.parseInt(splitEndDate[0]), Integer.parseInt(splitEndDate[1]) - 1, Integer.parseInt(splitEndDate[2]), 0, 0);
            List<ProductEntity> productList = productSessionBeanLocal.retrieveSpecificHistoricalTransactions(dStartDate, dEndDate, company.getCompanyId());
            for (ProductEntity p : productList) {
                p = nullifyProduct(p);

            }

            GenericEntity<List<ProductEntity>> genericEntity = new GenericEntity<List<ProductEntity>>(productList) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (CompanyDoesNotExistException | IncorrectLoginParticularsException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid account").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.FORBIDDEN).entity("ex.getMessage()" + ex.getMessage()).build();
        }
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
//        }
    }

    private ProductSessionBeanLocal lookupProductSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ProductSessionBeanLocal) c.lookup("java:global/MoolahEnterprise/MoolahEnterprise-ejb/ProductSessionBean!ejb.stateless.ProductSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CompanySessionBeanLocal lookupCompanySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CompanySessionBeanLocal) c.lookup("java:global/MoolahEnterprise/MoolahEnterprise-ejb/CompanySessionBean!ejb.stateless.CompanySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public ProductEntity nullifyProduct(ProductEntity product) {
//        if (product.getCompany() != null) {
//            product.getCompany().setListOfProducts(null);
//            if (product.getCompany().getRefund() != null) {
//                product.getCompany().getRefund().setCompany(null);
//            }
//            if (product.getCompany().getListOfPayments() != null && !product.getCompany().getListOfPayments().isEmpty()) {
//                for (PaymentEntity pay : product.getCompany().getListOfPayments()) {
//                    pay.setCompany(null);
//                    if (pay instanceof MonthlyPaymentEntity) {
//                        MonthlyPaymentEntity c = ((MonthlyPaymentEntity) pay);
//                        for (ProductLineItemEntity pl : c.getListOfProductLineItems()) {
//                            if (pl.getProduct() != null) {
//                                pl.setProduct(null);
//                            }
//                        }
//                    }
//
//                }
//                if (product.getCompany().getListOfPointOfContacts() != null && !product.getCompany().getListOfPointOfContacts().isEmpty()) {
//                    for (PointOfContactEntity poc : product.getCompany().getListOfPointOfContacts()) {
//                        poc.setCompany(null);
//
//                    }
//                }
//
//            }
//        }

        if (product.getCompany() != null) {
            product.getCompany().setListOfProducts(null);
            if (product.getCompany().getRefund() != null) {
                product.getCompany().getRefund().setCompany(null);
            }
            if (product.getCompany().getListOfPayments() != null && !product.getCompany().getListOfPayments().isEmpty()) {
                for (PaymentEntity pay : product.getCompany().getListOfPayments()) {
                    pay.setCompany(null);
                    if (pay instanceof MonthlyPaymentEntity) {
                        MonthlyPaymentEntity c = ((MonthlyPaymentEntity) pay);
                        for (ProductLineItemEntity pl : c.getListOfProductLineItems()) {
                            if (pl.getProduct() != null) {
                                pl.setProduct(null);
                            }
                        }
                    }
                }
            }
            if (product.getCompany().getListOfPointOfContacts() != null && !product.getCompany().getListOfPointOfContacts().isEmpty()) {
                for (PointOfContactEntity poc : product.getCompany().getListOfPointOfContacts()) {
                    poc.setCompany(null);

                }
            }

        }
        return product;
    }
}
