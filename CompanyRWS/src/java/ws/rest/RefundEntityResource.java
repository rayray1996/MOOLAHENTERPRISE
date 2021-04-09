/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.entity.RefundEntity;
import ejb.stateless.RefundSessionBeanLocal;
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
@Path("RefundEntity")
public class RefundEntityResource {

    RefundSessionBeanLocal refundSessionBeanLocal = lookupRefundSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RefundEntityResource
     */
    public RefundEntityResource() {
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewRecord(RefundEntity refundEntity) {
        if (refundEntity != null) {
            try {
                RefundEntity tempCE = refundSessionBeanLocal.createNewRefund(refundEntity);
                return Response.status(Response.Status.OK).entity(tempCE.getRefundId()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
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
