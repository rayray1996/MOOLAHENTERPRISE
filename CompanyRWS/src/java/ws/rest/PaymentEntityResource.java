/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.homeType;
import ejb.Singleton.MoolahCreditConverterLocal;
import ejb.entity.CompanyEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.PointOfContactEntity;
import ejb.entity.ProductEntity;
import ejb.entity.ProductLineItemEntity;
import ejb.stateless.CompanySessionBeanLocal;
import ejb.stateless.InvoiceSessionBeanLocal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CompanyDoesNotExistException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.InvalidPaymentEntityCreationException;
import util.exception.PaymentEntityAlreadyExistsException;
import util.exception.UnknownPersistenceException;

/**
 * REST Web Service
 *
 * @author sohqi
 */
@Path("PaymentEntity")
public class PaymentEntityResource {

    MoolahCreditConverterLocal moolahCreditConverter = lookupMoolahCreditConverterLocal();

    InvoiceSessionBeanLocal invoiceSessionBean = lookupInvoiceSessionBeanLocal();

    CompanySessionBeanLocal companySessionBeanLocal = lookupCompanySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Working Creates a new instance of PaymentEntityResource
     */
    public PaymentEntityResource() {
    }

    @Path("getCostOfCredit")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCostOfCredit(@QueryParam("email") String email, @QueryParam("password") String password) {
        BigDecimal cost = moolahCreditConverter.getCostOfCreditInSGD();

        GenericEntity<BigDecimal> genericEntity = new GenericEntity<BigDecimal>(cost) {
        };
        return Response.status(Status.OK).entity(genericEntity).build();
    }

    @Path("retrieveAllHistoricalTransactions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllHistoricalTransactions() {
        try {
            List<PaymentEntity> paymentEntity = companySessionBeanLocal.retrieveAllHistoricalTransactions();

            for (PaymentEntity mthlyPmt : paymentEntity) {
                if (mthlyPmt.getCompany() != null) {
                    mthlyPmt.getCompany().setListOfPayments(null);
                    if (mthlyPmt.getCompany().getRefund() != null) {
                        mthlyPmt.getCompany().getRefund().setCompany(null);

                    }
                    for (PointOfContactEntity poc : mthlyPmt.getCompany().getListOfPointOfContacts()) {
                        poc.setCompany(null);
                    }
                    for (ProductEntity product : mthlyPmt.getCompany().getListOfProducts()) {
                        product.setCompany(null);
                    }
                }

            }

            GenericEntity<List<PaymentEntity>> genericEntity = new GenericEntity<List<PaymentEntity>>(paymentEntity) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("*************error : " + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * Working
     *
     * @param email
     * @param password
     * @param startDate
     * @param endDate
     * @return
     */
    @Path("retrieveSpecificHistoricalTransactions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSpecificHistoricalTransactions(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
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
            List<PaymentEntity> paymentEntity = companySessionBeanLocal.retrieveSpecificHistoricalTransactions(dStartDate, dEndDate, company.getCompanyId());

            for (PaymentEntity pay : paymentEntity) {
                pay.getCompany().setListOfPayments(null);
                if (pay.getCompany().getListOfPointOfContacts() != null && !pay.getCompany().getListOfPointOfContacts().isEmpty()) {
                    for (PointOfContactEntity poc : pay.getCompany().getListOfPointOfContacts()) {
                        poc.setCompany(null);
                    }
                }
                if (pay.getCompany().getRefund() != null) {
                    pay.getCompany().getRefund().setCompany(null);
                }
                if (pay.getCompany().getListOfProducts() != null && !pay.getCompany().getListOfProducts().isEmpty()) {
                    for (ProductEntity product : pay.getCompany().getListOfProducts()) {
                        product.setCompany(null);
                    }
                }

            }
            GenericEntity<List<PaymentEntity>> genericEntity = new GenericEntity<List<PaymentEntity>>(paymentEntity) {
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

    /**
     *
     * working month : 2021-01-21
     *
     * @param month
     * @return
     */
    @Path("retrieveCurrentMonthlyPaymentEntity")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCurrentMonthlyPaymentEntity(@QueryParam("month") String month, @QueryParam("coyId") Long coyId) {
        Calendar dStartDate = new GregorianCalendar();

        try {
            String[] splitDate = month.split("-");
            String strYear = splitDate[0];
            String strMonth = splitDate[1];
            dStartDate.set(Integer.parseInt(strYear), Integer.parseInt(strMonth) - 1, 1);
            System.out.println("***********dStartDate " + dStartDate);
            MonthlyPaymentEntity mthlyPmt = companySessionBeanLocal.retrieveCurrentMonthlyPaymentEntity(dStartDate, coyId);

            if (mthlyPmt.getCompany() != null) {
                mthlyPmt.getCompany().setListOfPayments(null);
                if (mthlyPmt.getCompany().getRefund() != null) {
                    mthlyPmt.getCompany().getRefund().setCompany(null);

                }
                for (PointOfContactEntity poc : mthlyPmt.getCompany().getListOfPointOfContacts()) {
                    poc.setCompany(null);
                }
                for (ProductEntity product : mthlyPmt.getCompany().getListOfProducts()) {
                    product.setCompany(null);
                }

            }
            GenericEntity<MonthlyPaymentEntity> genericEntity = new GenericEntity<MonthlyPaymentEntity>(mthlyPmt) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("ex:" + ex.getMessage());
            System.out.println("time : " + dStartDate.getTime());
            System.out.println("error message:" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * working value for strYear : 2021
     */
    @Path("retrieveCurrentYearMonthlyPaymentEntity")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCurrentYearMonthlyPaymentEntity(@QueryParam("strYear") String year, @QueryParam("coyId") Long coyId) {
        try {
            Calendar dStartDate = new GregorianCalendar();
            dStartDate.set(Integer.valueOf(year), 0, 1);
            List<MonthlyPaymentEntity> mthlyPmts = companySessionBeanLocal.retrieveCurrentYearMonthlyPaymentEntity(dStartDate, coyId);

            for (MonthlyPaymentEntity mthlyPmt : mthlyPmts) {
                if (mthlyPmt.getCompany() != null) {
                    mthlyPmt.getCompany().setListOfPayments(null);
                    if (mthlyPmt.getCompany().getRefund() != null) {
                        mthlyPmt.getCompany().getRefund().setCompany(null);

                    }
                    for (PointOfContactEntity poc : mthlyPmt.getCompany().getListOfPointOfContacts()) {
                        poc.setCompany(null);
                    }
                    for (ProductEntity product : mthlyPmt.getCompany().getListOfProducts()) {
                        product.setCompany(null);
                    }
                }

            }

            GenericEntity<List<MonthlyPaymentEntity>> genericEntity = new GenericEntity<List<MonthlyPaymentEntity>>(mthlyPmts) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("ex.message()" + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    /**
     * Working
     *
     * @param email
     * @param password
     * @param companyId
     * @param paymentId
     * @return
     */
    @GET
    @Path("makePayment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response makePayment(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("paymentId") Long paymentId) {

        try {
            CompanyEntity company = companySessionBeanLocal.login(email, password);
            if (company != null) {
                invoiceSessionBean.makePayment(paymentId, company.getCompanyId());

                return Response.status(Response.Status.OK).entity("").build();
            } else {

                return Response.status(Response.Status.BAD_REQUEST).entity("Incorrect email and password").build();
            }

        } catch (Exception ex) {

            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }

    }

    /**
     * Working
     *
     * @param email
     * @param password
     * @return
     */
    @Path("purchaseMoolahCredits")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response purchaseMoolahCredits(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("creditToBuy") BigInteger creditToBuy) {
        try {
            CompanyEntity company = companySessionBeanLocal.login(email, password);
            Long paymentId = null;
            if (company != null) {
                paymentId = invoiceSessionBean.purchaseMoolahCredits(company.getCompanyId(), creditToBuy);
            }
            return Response.status(Response.Status.OK).entity(paymentId).build();
        } catch (CompanyDoesNotExistException | IncorrectLoginParticularsException | InvalidPaymentEntityCreationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid purchase due incorrect company information: " + ex.getMessage()).build();
        } catch (UnknownPersistenceException | PaymentEntityAlreadyExistsException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid purchase due incorrect invalid payment: " + ex.getMessage()).build();

        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveAllUnpaidMonthlyPayment")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllUnpaidMonthlyPayment(@QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            CompanyEntity company = companySessionBeanLocal.login(email, password);

            List<MonthlyPaymentEntity> paymentEntity = companySessionBeanLocal.retrieveAllUnpaidPayment(email);

            for (MonthlyPaymentEntity mthlyPmt : paymentEntity) {
                if (mthlyPmt.getCompany() != null) {
                    mthlyPmt.getCompany().setListOfPayments(null);
                    if (mthlyPmt.getCompany().getRefund() != null) {
                        mthlyPmt.getCompany().getRefund().setCompany(null);

                    }
                    for (PointOfContactEntity poc : mthlyPmt.getCompany().getListOfPointOfContacts()) {
                        poc.setCompany(null);
                    }
                    for (ProductEntity product : mthlyPmt.getCompany().getListOfProducts()) {
                        product.setCompany(null);
                    }
                }

            }

            GenericEntity<List<MonthlyPaymentEntity>> genericEntity = new GenericEntity<List<MonthlyPaymentEntity>>(paymentEntity) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("*************error : " + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveMonthlyPaymentById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveMonthlyPaymentById(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("id") Long id) {
        try {
            CompanyEntity company = companySessionBeanLocal.login(email, password);

            MonthlyPaymentEntity paymentEntity = invoiceSessionBean.retrieveMonthlyPaymentById(id);

            if (paymentEntity.getCompany() != null) {
                paymentEntity.getCompany().setListOfPayments(null);
                if (paymentEntity.getCompany().getRefund() != null) {
                    paymentEntity.getCompany().getRefund().setCompany(null);

                }
                for (PointOfContactEntity poc : paymentEntity.getCompany().getListOfPointOfContacts()) {
                    poc.setCompany(null);
                }
                for (ProductEntity product : paymentEntity.getCompany().getListOfProducts()) {
                    product.setCompany(null);
                }
            }

            GenericEntity<MonthlyPaymentEntity> genericEntity = new GenericEntity<MonthlyPaymentEntity>(paymentEntity) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("*************error : " + ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
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

    private InvoiceSessionBeanLocal lookupInvoiceSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (InvoiceSessionBeanLocal) c.lookup("java:global/MoolahEnterprise/MoolahEnterprise-ejb/InvoiceSessionBean!ejb.stateless.InvoiceSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private MoolahCreditConverterLocal lookupMoolahCreditConverterLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (MoolahCreditConverterLocal) c.lookup("java:global/MoolahEnterprise/MoolahEnterprise-ejb/MoolahCreditConverter!ejb.Singleton.MoolahCreditConverterLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
