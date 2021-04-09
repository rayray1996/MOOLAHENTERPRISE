/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.PointOfContactEntity;
import ejb.entity.ProductEntity;
import ejb.entity.ProductLineItemEntity;
import ejb.stateless.CompanySessionBeanLocal;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service
 *
 * @author sohqi
 */
@Path("PaymentEntity")
public class PaymentEntityResource {

    CompanySessionBeanLocal companySessionBeanLocal = lookupCompanySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PaymentEntityResource
     */
    public PaymentEntityResource() {
    }

    @Path("retrieveAllHistoricalTransactions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllHistoricalTransactions() {
        try {
            List<PaymentEntity> paymentEntity = companySessionBeanLocal.retrieveAllHistoricalTransactions();
            for (PaymentEntity ce : paymentEntity) {
                ce.getCompany();
                for (ProductEntity product : ce.getCompany().getListOfProducts()) {
                    product.getCompany().setListOfProducts(null);
                }
                ce.getCompany().setListOfPayments(null);

                for (PointOfContactEntity poc : ce.getCompany().getListOfPointOfContacts()) {
                    poc.getCompany().setListOfPointOfContacts(null);
                }

            }
            GenericEntity<List<PaymentEntity>> genericEntity = new GenericEntity<List<PaymentEntity>>(paymentEntity) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

//    @Path("retrieveSpecificHistoricalTransactions")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response retrieveSpecificHistoricalTransactions(java.sql.Date startDate, java.sql.Date endDate) {
//        try {
//            Calendar dStartDate = new GregorianCalendar();
//            dStartDate.setTime(startDate);
//            Calendar dEndDate = new GregorianCalendar();
//            dEndDate.setTime(endDate);
//            List<PaymentEntity> paymentEntity = companySessionBeanLocal.retrieveSpecificHistoricalTransactions(dStartDate, dEndDate);
//            for (PaymentEntity ce : paymentEntity) {
//                ce.getCompany();
//                for (ProductEntity product : ce.getCompany().getListOfProducts()) {
//                    product.getCompany().setListOfProducts(null);
//                }
//                ce.getCompany().setListOfPayments(null);
//
//                for (PointOfContactEntity poc : ce.getCompany().getListOfPointOfContacts()) {
//                    poc.getCompany().setListOfPointOfContacts(null);
//                }
//
//            }
//            GenericEntity<List<PaymentEntity>> genericEntity = new GenericEntity<List<PaymentEntity>>(paymentEntity) {
//            };
//
//            return Response.status(Status.OK).entity(genericEntity).build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
//        }
//    }
//
//    @Path("retrieveCurrentMonthlyPaymentEntity")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response retrieveCurrentMonthlyPaymentEntity(java.sql.Date month) {
//        try {
//            Calendar dStartDate = new GregorianCalendar();
//            dStartDate.setTime(month);
//            MonthlyPaymentEntity mthlyPmt = companySessionBeanLocal.retrieveCurrentMonthlyPaymentEntity(dStartDate);
//            for (ProductLineItemEntity ple : mthlyPmt.getListOfProductLineItems()) {
//                ple.getProduct();
//                ple.getProduct().getCompany().setListOfProducts(null);
//
//            }
//            GenericEntity<MonthlyPaymentEntity> genericEntity = new GenericEntity<MonthlyPaymentEntity>(mthlyPmt) {
//            };
//
//            return Response.status(Status.OK).entity(genericEntity).build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
//        }
//    }
//
//    @Path("retrieveCurrentYearMonthlyPaymentEntity")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response retrieveCurrentYearMonthlyPaymentEntity(java.sql.Date month) {
//        try {
//            Calendar dStartDate = new GregorianCalendar();
//            dStartDate.setTime(month);
//            List<MonthlyPaymentEntity> mthlyPmts = companySessionBeanLocal.retrieveCurrentYearMonthlyPaymentEntity(dStartDate);
//            for (MonthlyPaymentEntity mthlyPmt : mthlyPmts) {
//                for (ProductLineItemEntity ple : mthlyPmt.getListOfProductLineItems()) {
//                    ple.getProduct();
//                    ple.getProduct().getCompany().setListOfProducts(null);
//
//                }
//            }
//            GenericEntity<List<MonthlyPaymentEntity>> genericEntity = new GenericEntity<List<MonthlyPaymentEntity>>(mthlyPmts) {
//            };
//
//            return Response.status(Status.OK).entity(genericEntity).build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
//        }
//    }

    private CompanySessionBeanLocal lookupCompanySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CompanySessionBeanLocal) c.lookup("java:global/MoolahEnterprise/MoolahEnterprise-ejb/CompanySessionBean!ejb.stateless.CompanySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
