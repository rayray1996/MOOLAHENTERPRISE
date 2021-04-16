package ws.restful;

import ejb.entity.CompanyEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.PaymentEntity;
import ejb.entity.PointOfContactEntity;
import ejb.entity.ProductEntity;
import ejb.entity.ProductLineItemEntity;
import ejb.stateless.CompanySessionBeanLocal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import util.exception.CompanyDoesNotExistException;
import util.exception.CompanySQLConstraintException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.UnknownPersistenceException;

@Path("File")

public class FileResource {

    CompanySessionBeanLocal companySessionBean = lookupCompanySessionBeanLocal();

    @Context
    private UriInfo context;
    @Context
    private ServletContext servletContext;

    public FileResource() {
    }

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@FormDataParam("file") InputStream uploadedFileInputStream,
            @FormDataParam("file") FormDataContentDisposition uploadedFileDetails,
            @QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            System.err.println("********** FileResource.upload()");

            CompanyEntity company = companySessionBean.login(email, password);
            String companyId = company.getCompanyId().toString();
            String fileName = uploadedFileDetails.getFileName();

            int index = fileName.lastIndexOf('.');
            String extension = "";
            if (index > 0) {
                extension = fileName.substring(index + 1);
                System.out.println("File extension is " + extension);
            }

            if (extension.equals("")) {
                return Response.status(Status.BAD_REQUEST).entity("Invalid file type").build();
            }
            String outputFilePath = servletContext.getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + companyId + "." + extension;
            File file = new File(outputFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                a = uploadedFileInputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            uploadedFileInputStream.close();
            company.setCompanyImage(companyId + "." + extension);
            companySessionBean.updateCompanyInformation(company);
            company = nullifyCompany(company);
            return Response.status(Status.OK).entity(company).build();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

            return Response.status(Status.FORBIDDEN).entity("file processing error").build();
        } catch (IOException ex) {
            ex.printStackTrace();

            return Response.status(Status.BAD_GATEWAY).entity("file processing error").build();
        } catch (CompanyDoesNotExistException | IncorrectLoginParticularsException | UnknownPersistenceException | CompanySQLConstraintException ex) {
            return Response.status(Status.BAD_REQUEST).entity("You are not logged in").build();
        }
    }

    private CompanyEntity nullifyCompany(CompanyEntity company) {
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
