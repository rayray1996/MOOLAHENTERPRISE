/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.entity.CreditPaymentEntity;
import ejb.entity.EndowmentEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.PointOfContactEntity;
import ejb.entity.ProductEntity;
import ejb.entity.ProductLineItemEntity;
import ejb.entity.TermLifeProductEntity;
import ejb.entity.WholeLifeProductEntity;
import ejb.stateless.ProductSessionBeanLocal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ws.datamodel.ProductWrapper;

/**
 * REST Web Service
 *
 * @author sohqi
 */
@Path("Product")
public class ProductResource {

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
 * @return 
 */
    @Path("retrieveAllEndowmentProducts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllEndowmentProducts() {
        try {
            List<EndowmentEntity> products = productSessionBeanLocal.retrieveAllEndowmentProducts();
            for (ProductEntity product : products) {

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
     * @return 
     */

    @Path("retrieveAllTermLifeProducts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTermLifeProducts() {
        try {
            List<TermLifeProductEntity> products = productSessionBeanLocal.retrieveAllTermLifeProducts();
            for (ProductEntity product : products) {

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
 * @return 
 */
    @Path("retrieveAllWholeLifeProducts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllWholeLifeProducts() {
        try {
            List<WholeLifeProductEntity> products = productSessionBeanLocal.retrieveAllWholeLifeProducts();
            for (ProductEntity product : products) {

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
     * @param productId
     * @return 
     */
    @Path("retrieveProductEntityById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveProductEntityById(@QueryParam("productId") Long productId) {
        try {
            ProductEntity product = productSessionBeanLocal.retrieveProductEntityById(productId);

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
                    if (product.getCompany().getListOfPointOfContacts() != null && !product.getCompany().getListOfPointOfContacts().isEmpty()) {
                        for (PointOfContactEntity poc : product.getCompany().getListOfPointOfContacts()) {
                            poc.setCompany(null);

                        }
                    }

                }
            }
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
                        if (product.getCompany().getListOfPointOfContacts() != null && !product.getCompany().getListOfPointOfContacts().isEmpty()) {
                            for (PointOfContactEntity poc : product.getCompany().getListOfPointOfContacts()) {
                                poc.setCompany(null);

                            }
                        }

                    }
                }
            }
           GenericEntity<List<ProductEntity>> genericEntity = new GenericEntity<List<ProductEntity>>(products) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("***********" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    
    @Path("filterProductsByCriteria")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterProductsByCriteria(ProductWrapper productWrapper) {
        try {
          // CategoryEnum category, Boolean wantsRider, Boolean isSmoker, BigDecimal sumAssured, Integer coverageTerm, Integer premiumTerm, EndowmentProductEnum endowmentProductEnum, 
        //           TermLifeProductEnum termLifeProductEnum, WholeLifeProductEnum wholeLifeProductEnum
            List<ProductEntity> products = productSessionBeanLocal.filterProductsByCriteria(productWrapper.getCategory(),productWrapper.getWantsRider(),productWrapper.getIsSmoker(),
                    productWrapper.getSumAssured(), productWrapper.getCoverageTerm(),productWrapper.getPremiumTerm(),productWrapper.getEndowmentProductEnum(), productWrapper.getTermLifeProductEnum(), productWrapper.getWholeLifeProductEnum());
          
           //  List<ProductEntity> products = productSessionBeanLocal.searchForProductsByName("");
             for (ProductEntity product : products) {

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
                        if (product.getCompany().getListOfPointOfContacts() != null && !product.getCompany().getListOfPointOfContacts().isEmpty()) {
                            for (PointOfContactEntity poc : product.getCompany().getListOfPointOfContacts()) {
                                poc.setCompany(null);

                            }
                        }

                    }
                }
            }
           GenericEntity<List<ProductEntity>> genericEntity = new GenericEntity<List<ProductEntity>>(products) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("***********" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
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
}
