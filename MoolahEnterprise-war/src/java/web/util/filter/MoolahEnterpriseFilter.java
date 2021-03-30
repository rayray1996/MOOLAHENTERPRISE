/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.util.filter;

import ejb.entity.CustomerEntity;
import ejb.stateless.CustomerSessionBeanLocal;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import managedbean.ViewProductDetailManagedBean;
import util.exception.CustomerDoesNotExistsException;

/**
 *
 * @author nickg
 */
@WebFilter(filterName = "MoolahEnterpriseFilter", urlPatterns = {"/*"})
public class MoolahEnterpriseFilter implements Filter {

    CustomerSessionBeanLocal customerSessionBean = lookupCustomerSessionBeanLocal();

    private static final boolean debug = true;

    FilterConfig filterConfig;

    private static final String CONTEXT_ROOT = "/MoolahEnterprise-war";

    public MoolahEnterpriseFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();

        if (httpSession.getAttribute("isLogin") == null) {
            httpSession.setAttribute("isLogin", false);
        }

        Boolean isLogin = (Boolean) httpSession.getAttribute("isLogin");

        if (!excludeLoginCheck(requestServletPath)) {
            if (isLogin == true) {

                chain.doFilter(request, response);

            } else {
                checkCustomerLink(requestServletPath, httpServletResponse, httpSession);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {

    }

    private Boolean excludeLoginCheck(String path) {
        if (path.equals("/index.xhtml")
                || path.equals("/accessRightError.xhtml")
                || path.startsWith("/javax.faces.resource")
                || path.startsWith("/aboutUs.xhtml")
                || path.startsWith("/resetPassword.xhtml")
                || path.startsWith("/createAccount.xhtml")
                || path.startsWith("/product/ViewRecommendedProduct.xhtml")
                || path.startsWith("/product/viewProductDetail.xhtml")) {
            return true;
        } else {
            return false;
        }
    }

    public void checkCustomerLink(String requestServletPath, HttpServletResponse httpServletResponse, HttpSession httpSession) throws IOException {

        try {
            if (requestServletPath.startsWith("?Param")) {

                String[] requestServletPathElements = requestServletPath.split("?");
                String path = requestServletPathElements[1];
                CustomerEntity customer = customerSessionBean.retrieveCustomerByParaLink(path);
                httpSession.setAttribute("changePwCust", customer);

                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/keyPassword.xhtml");
            } else {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
            }
        } catch (CustomerDoesNotExistsException ex) {
            httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
        }
    }

    private CustomerSessionBeanLocal lookupCustomerSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (CustomerSessionBeanLocal) c.lookup("java:global/MoolahEnterprise/MoolahEnterprise-ejb/CustomerSessionBean!ejb.stateless.CustomerSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
