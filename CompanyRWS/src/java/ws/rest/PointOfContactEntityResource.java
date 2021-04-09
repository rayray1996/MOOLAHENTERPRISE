/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.entity.CompanyEntity;
import ejb.entity.PointOfContactEntity;
import ejb.stateless.PointOfContactSessionBeanLocal;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author sohqi
 */
@Path("PointOfContactEntity")
public class PointOfContactEntityResource {

    PointOfContactSessionBeanLocal pointOfContactSessionBeanLocal = lookupPointOfContactSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PointOfContactEntityResource
     */
    public PointOfContactEntityResource() {
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewRecord(PointOfContactEntity newRecord) {
        if (newRecord != null) {
            try {
                Long tempRecordId = pointOfContactSessionBeanLocal.createNewPointOfContact(newRecord);
                newRecord.getCompany().setListOfPointOfContacts(null);

                return Response.status(Response.Status.OK).entity(tempRecordId).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }

    private PointOfContactSessionBeanLocal lookupPointOfContactSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PointOfContactSessionBeanLocal) c.lookup("java:global/MoolahEnterprise/MoolahEnterprise-ejb/PointOfContactSessionBean!ejb.stateless.PointOfContactSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
