/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.entity.CompanyEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.PointOfContactEntity;
import ejb.entity.ProductEntity;
import ejb.entity.ProductLineItemEntity;
import ejb.entity.RefundEntity;
import ejb.entity.RiderEntity;
import ejb.stateless.CompanySessionBeanLocal;
import ejb.stateless.InvoiceSessionBeanLocal;
import ejb.stateless.RefundSessionBeanLocal;
import ejb.stateless.RiderSessionBeanLocal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
import util.exception.CompanyAlreadyExistException;
import util.exception.CompanyBeanValidaionException;
import util.exception.CompanyCreationException;
import util.exception.CompanyDoesNotExistException;
import util.exception.CompanySQLConstraintException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.InvalidOTPException;
import util.exception.InvalidPaymentEntityCreationException;
import util.exception.PaymentEntityAlreadyExistsException;
import util.exception.PointOfContactBeanValidationException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;
import ws.datamodel.CompanyCreateWrapper;
import ws.datamodel.CompanyUpdateWrapper;
import ws.datamodel.UploadPath;

/**
 * REST Web Service
 *
 * @author sohqi
 */
@Path("Company")
public class CompanyResource {

    RefundSessionBeanLocal refundSessionBean = lookupRefundSessionBeanLocal();

    InvoiceSessionBeanLocal invoiceSessionBean = lookupInvoiceSessionBeanLocal();

    RiderSessionBeanLocal riderSessionBeanLocal = lookupRiderSessionBeanLocal();

    CompanySessionBeanLocal companySessionBeanLocal = lookupCompanySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CompanyResource
     */
    public CompanyResource() {
    }

    @Path("retrieveAllRecords")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRecords() {
        try {
            List<CompanyEntity> companies = companySessionBeanLocal.retrieveAllActiveCompanies();
            for (CompanyEntity company : companies) {
                company = nullifyCompany(company);
//                for (ProductEntity product : company.getListOfProducts()) {
//                    product.setCompany(null);
//
//                }
//                if (company.getRefund() != null) {
//                    company.getRefund().setCompany(null);
//                }
//                for (PaymentEntity payment : company.getListOfPayments()) {
//                    payment.setCompany(null);
//                }
//                for (PointOfContactEntity pointOfContact : company.getListOfPointOfContacts()) {
//                    pointOfContact.setCompany(null);
//                }
            }
            GenericEntity<List<CompanyEntity>> genericEntity = new GenericEntity<List<CompanyEntity>>(companies) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveAllRecordsById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRecordsById(@QueryParam("email") String email) {
        try {
            CompanyEntity company = companySessionBeanLocal.retrieveCompanyByEmail(email);
            company = nullifyCompany(company);
//            for (ProductEntity product : company.getListOfProducts()) {
//                product.setCompany(null);
//
//            }
//            if (company.getRefund() != null) {
//                company.getRefund().setCompany(null);
//            }
//            for (PaymentEntity payment : company.getListOfPayments()) {
//                payment.setCompany(null);
//            }
//            for (PointOfContactEntity pointOfContact : company.getListOfPointOfContacts()) {
//                pointOfContact.setCompany(null);
//            }

            GenericEntity<CompanyEntity> genericEntity = new GenericEntity<CompanyEntity>(company) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("login")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRecordsByLogin(@QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            System.out.println("email : " + email + " password" + password);
            CompanyEntity company = companySessionBeanLocal.login(email, password);

            company = nullifyCompany(company);
//            for (ProductEntity product : company.getListOfProducts()) {
//                product.setCompany(null);
//
//            }
//            if (company.getRefund() != null) {
//                company.getRefund().setCompany(null);
//            }
//            for (PaymentEntity payment : company.getListOfPayments()) {
//                payment.setCompany(null);
//            }
//            for (PointOfContactEntity pointOfContact : company.getListOfPointOfContacts()) {
//                pointOfContact.setCompany(null);
//            }

            GenericEntity<CompanyEntity> genericEntity = new GenericEntity<CompanyEntity>(company) {
            };
            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("updateCompanyInformation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCompanyInformation(CompanyEntity company, @QueryParam("email") String email, @QueryParam("password") String password) {
        if (company != null) {
            try {

                CompanyEntity tempCompanyEntity = companySessionBeanLocal.updateCompanyInformationWS(company);
                tempCompanyEntity = nullifyCompany(company);
//                for (ProductEntity product : tempCompanyEntity.getListOfProducts()) {
//                    product.setCompany(null);
//                }
//                if (tempCompanyEntity.getRefund() != null) {
//                    company.getRefund().setCompany(null);
//                }
//                for (PaymentEntity payment : tempCompanyEntity.getListOfPayments()) {
//                    payment.setCompany(null);
//                }
//                for (PointOfContactEntity pointOfContact : tempCompanyEntity.getListOfPointOfContacts()) {
//                    pointOfContact.setCompany(null);
//                }

                return Response.status(Response.Status.OK).entity(tempCompanyEntity).build();
            } catch (UnknownPersistenceException | CompanySQLConstraintException | PointOfContactBeanValidationException | CompanyBeanValidaionException ex) {
                System.out.println("ex.message" + ex.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                System.out.println("ex.message" + ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }

    }

    @Path("createNewRecord")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewRecord(CompanyCreateWrapper newRecord) {
        if (newRecord != null) {
            try {
                CompanyEntity tempCompany = newRecord.getCompanyEntity();
                tempCompany.setListOfPayments(null);
                tempCompany.setListOfProducts(null);
                tempCompany.setRefund(null);
                if (newRecord.getListOfPointOfContacts() != null && !newRecord.getListOfPointOfContacts().isEmpty()) {

                    tempCompany.setListOfPointOfContacts(newRecord.getListOfPointOfContacts());
                    for (PointOfContactEntity poc : tempCompany.getListOfPointOfContacts()) {
                        poc.setCompany(null);
                    }
                }

                tempCompany.setSalt(CryptographicHelper.getInstance().generateRandomString(32));
                tempCompany.setPasswordHash(tempCompany.getPassword());
                tempCompany.setCompanyImage("Employee-256.png");
                CompanyEntity company = companySessionBeanLocal.createAccountForCompanyWS(tempCompany);

                company = nullifyCompany(company);

                return Response.status(Response.Status.OK).entity(company.getCompanyId()).build();
            } catch (CompanyAlreadyExistException | UnknownPersistenceException | CompanyCreationException | PointOfContactBeanValidationException ex) {
                System.out.println("Error at 1");
                return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                System.out.println("ex.message" + ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            System.out.println("Error at 2");
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }

    @Path("retrieveUploadPath")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveUploadPath() {
//        String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1");
//        String newFilePath = "C:/glassfish-5.1.0-uploadedfiles/uploadedFiles";
        String newFilePath = "MoolahEnterprise-war/uploadedFiles";
//        System.out.println("newFilePath = " + newFilePath);
        if (newFilePath != null && !newFilePath.equals("")) {
            GenericEntity<String> genericEntity = new GenericEntity<String>(newFilePath) {
            };
            return Response.status(Status.OK).entity(new UploadPath(newFilePath)).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Path does not exist").build();
        }

    }

    /**
     * Working
     *
     * @param email
     * @param password
     * @return
     */
    @Path("deactivateAccount")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateAccount(@QueryParam("email") String email,
            @QueryParam("password") String password
    ) {
        try {
            CompanyEntity company = companySessionBeanLocal.login(email, password);
            if (company != null) {
                companySessionBeanLocal.deactivateAccount(email);
            }
            return Response.status(Response.Status.OK).entity("").build();
        } catch (CompanyDoesNotExistException | IncorrectLoginParticularsException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid account").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.FORBIDDEN).entity(ex.getMessage()).build();
        }
    }

    /**
     * not tested
     *
     * @param email
     * @param password
     * @return
     */
    @Path("sendOTP")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(@QueryParam("email") String email
    ) {
        try {
            companySessionBeanLocal.resetPassword(email);

            return Response.status(Response.Status.OK).entity(true).build();
        } catch (CompanyDoesNotExistException ex) {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid account").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.FORBIDDEN).entity(ex.getMessage()).build();
        }
    }

    /**
     * not tested
     *
     * @param email
     * @param password
     * @return
     */
    @Path("resetCompanyPassword")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCompanyByOTP(@QueryParam("email") String email,
            @QueryParam("otp") String otp,
            @QueryParam("newPassword") String newPassword,
            @QueryParam("repeatPassword") String repeatPassword
    ) {
        try {
            CompanyEntity company = companySessionBeanLocal.retrieveCompanyByOTP(email, otp);
            if (repeatPassword != null && newPassword != null) {

                if (!newPassword.equals(repeatPassword)) {
                    return Response.status(Status.CONFLICT).entity("New passwords do not match").build();
                }

                String salt = company.getSalt();

                String newPasswordSalted = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(newPassword + salt));
                company.setPassword(newPasswordSalted);
                CompanyEntity tempCompanyEntity = companySessionBeanLocal.updateCompanyInformationWS(company);

                return Response.status(Response.Status.OK).entity(true).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("Repeat password and new password must not be empty").build();
            }
        } catch (InvalidOTPException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("updateCompanyPassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCompanyPassword(CompanyEntity updateCompany,
            @QueryParam("email") String email,
            @QueryParam("password") String password,
            @QueryParam("oldPassword") String oldPassword,
            @QueryParam("newPassword") String newPassword,
            @QueryParam("repeatNewPassword") String repeatNewPassword
    ) {
        System.out.println("updateCompany = " + updateCompany);
        System.out.println("oldPassword = " + oldPassword);
        System.out.println("newPassword = " + newPassword);
        System.out.println("repeatNewPassword = " + repeatNewPassword);
        if (updateCompany != null && oldPassword != null && newPassword != null && repeatNewPassword != null) {
            try {
                String salt = updateCompany.getSalt();
                String oldSaltedPassword = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(oldPassword + salt));

                if (!oldSaltedPassword.equals(updateCompany.getPassword())) {
                    return Response.status(Status.BAD_REQUEST).entity("Old password does not match current password").build();
                }

                if (!newPassword.equals(repeatNewPassword)) {
                    return Response.status(Status.BAD_REQUEST).entity("New passwords do not match").build();
                }

                String newPasswordSalted = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(newPassword + salt));
                updateCompany.setPassword(newPasswordSalted);
                CompanyEntity tempCompanyEntity = companySessionBeanLocal.updateCompanyInformationWS(updateCompany);

                for (ProductEntity product : tempCompanyEntity.getListOfProducts()) {
                    product.setCompany(null);
                }
                if (tempCompanyEntity.getRefund() != null) {
                    tempCompanyEntity.getRefund().setCompany(null);
                }
                for (PaymentEntity payment : tempCompanyEntity.getListOfPayments()) {
                    payment.setCompany(null);
                }
                for (PointOfContactEntity pointOfContact : tempCompanyEntity.getListOfPointOfContacts()) {
                    pointOfContact.setCompany(null);
                }

                return Response.status(Response.Status.OK).entity(tempCompanyEntity).build();
            } catch (UnknownPersistenceException | CompanySQLConstraintException | PointOfContactBeanValidationException | CompanyBeanValidaionException ex) {
                System.out.println("ex.message" + ex.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                System.out.println("ex.message" + ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid change password request").build();
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

    private RiderSessionBeanLocal lookupRiderSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RiderSessionBeanLocal) c.lookup("java:global/MoolahEnterprise/MoolahEnterprise-ejb/RiderSessionBean!ejb.stateless.RiderSessionBeanLocal");
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

    public CompanyEntity nullifyCompany(CompanyEntity company) {
        for (ProductEntity product : company.getListOfProducts()) {
            product.setCompany(null);
        }
        if (company.getRefund() != null) {
            company.getRefund().setCompany(null);
        }
        for (PaymentEntity payment : company.getListOfPayments()) {
            payment.setCompany(null);
            if(payment instanceof MonthlyPaymentEntity){
               for(ProductLineItemEntity prod: ((MonthlyPaymentEntity) payment).getListOfProductLineItems()){
                   prod.getProduct().setCompany(null);
               }
            }
        }
        for (PointOfContactEntity pointOfContact : company.getListOfPointOfContacts()) {
            pointOfContact.setCompany(null);
        }
        
        return company;
    }

    private RefundSessionBeanLocal lookupRefundSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RefundSessionBeanLocal) c.lookup("java:global/MoolahEnterprise/MoolahEnterprise-ejb/RefundSessionBean!ejb.stateless.RefundSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
