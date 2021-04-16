/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import java.util.Set;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 *
 * @author sohqi
 */
@javax.ws.rs.ApplicationPath("Resources")
public class ApplicationConfig extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        
        resources.add(MultiPartFeature.class);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.rest.CompanyCorsFilter.class);
        resources.add(ws.rest.CompanyResource.class);
        resources.add(ws.rest.PaymentEntityResource.class);
        resources.add(ws.rest.PointOfContactEntityResource.class);
        resources.add(ws.rest.ProductResource.class);
        resources.add(ws.rest.RefundEntityResource.class);
        resources.add(ws.restful.FileResource.class);
    }
    
}
