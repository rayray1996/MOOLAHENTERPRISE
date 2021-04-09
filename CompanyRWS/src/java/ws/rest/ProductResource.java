/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.entity.ProductEntity;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
 @Path("retrieveAllFinancialProducts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllFinancialProducts() {
        try {
            List<ProductEntity> companyEntity = productSessionBeanLocal.retrieveAllFinancialProducts();
//            for(CompanyEntity ce: companyEntity){
//                ce.setRefund(null);
//                ce.setListOfPayments(null);
//                ce.setListOfPointOfContacts(null);
//                ce.setListOfProducts(null);
//                
//            }
            GenericEntity<List<ProductEntity>> genericEntity = new GenericEntity<List<ProductEntity>>(companyEntity) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
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
