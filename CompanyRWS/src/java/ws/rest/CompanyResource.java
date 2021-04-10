/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.entity.CompanyEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.PointOfContactEntity;
import ejb.entity.ProductEntity;
import ejb.entity.RiderEntity;
import ejb.stateless.CompanySessionBeanLocal;
import ejb.stateless.RiderSessionBeanLocal;
import java.util.ArrayList;
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
import util.exception.CompanyAlreadyExistException;
import util.exception.CompanyBeanValidaionException;
import util.exception.CompanyCreationException;
import util.exception.CompanySQLConstraintException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.PointOfContactBeanValidationException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;
import ws.datamodel.CompanyCreateWrapper;
import ws.datamodel.CompanyUpdateWrapper;

/**
 * REST Web Service
 *
 * @author sohqi
 */
@Path("Company")
public class CompanyResource {

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
                for (ProductEntity product : company.getListOfProducts()) {
                    product.setCompany(null);

                }
                if (company.getRefund() != null) {
                    company.getRefund().setCompany(null);
                }
                for (PaymentEntity payment : company.getListOfPayments()) {
                    payment.setCompany(null);
                }
                for (PointOfContactEntity pointOfContact : company.getListOfPointOfContacts()) {
                    pointOfContact.setCompany(null);
                }
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
            for (ProductEntity product : company.getListOfProducts()) {
                product.setCompany(null);

            }
            if (company.getRefund() != null) {
                company.getRefund().setCompany(null);
            }
            for (PaymentEntity payment : company.getListOfPayments()) {
                payment.setCompany(null);
            }
            for (PointOfContactEntity pointOfContact : company.getListOfPointOfContacts()) {
                pointOfContact.setCompany(null);
            }

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
            CompanyEntity company = companySessionBeanLocal.login(email, password);

            for (ProductEntity product : company.getListOfProducts()) {
                product.setCompany(null);

            }
            if (company.getRefund() != null) {
                company.getRefund().setCompany(null);
            }
            for (PaymentEntity payment : company.getListOfPayments()) {
                payment.setCompany(null);
            }
            for (PointOfContactEntity pointOfContact : company.getListOfPointOfContacts()) {
                pointOfContact.setCompany(null);
            }

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
    public Response updateCompanyInformation(@QueryParam("email") String email, @QueryParam("password") String password, CompanyEntity company) {
        if (company != null) {
            try {

                CompanyEntity tempCompanyEntity = companySessionBeanLocal.updateCompanyInformationWS(company, email, password);

                for (ProductEntity product : tempCompanyEntity.getListOfProducts()) {
                    product.setCompany(null);
                }
                if (tempCompanyEntity.getRefund() != null) {
                    company.getRefund().setCompany(null);
                }
                for (PaymentEntity payment : tempCompanyEntity.getListOfPayments()) {
                    payment.setCompany(null);
                }
                for (PointOfContactEntity pointOfContact : tempCompanyEntity.getListOfPointOfContacts()) {
                    pointOfContact.setCompany(null);
                }

                return Response.status(Response.Status.OK).entity(tempCompanyEntity).build();
            } catch (UnknownPersistenceException | CompanySQLConstraintException | PointOfContactBeanValidationException | CompanyBeanValidaionException | IncorrectLoginParticularsException ex) {
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
                tempCompany.setPassword(tempCompany.getPassword());

                CompanyEntity company = companySessionBeanLocal.createAccountForCompanyWS(tempCompany);

                for (ProductEntity product : company.getListOfProducts()) {
                    product.setCompany(null);
                }
                if (company.getRefund() != null) {
                    company.getRefund().setCompany(null);
                }
                for (PaymentEntity payment : company.getListOfPayments()) {
                    payment.setCompany(null);
                }
                for (PointOfContactEntity pointOfContact : company.getListOfPointOfContacts()) {
                    pointOfContact.setCompany(null);
                }

                return Response.status(Response.Status.OK).entity(company).build();
            } catch (CompanyAlreadyExistException | UnknownPersistenceException | CompanyCreationException | PointOfContactBeanValidationException ex) {
                return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                System.out.println("ex.message" + ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
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
}
