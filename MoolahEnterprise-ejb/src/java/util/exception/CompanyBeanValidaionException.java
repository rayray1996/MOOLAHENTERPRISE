/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author sohqi
 */
public class CompanyBeanValidaionException extends Exception {

    /**
     * Creates a new instance of <code>CompanyBeanValidaionException</code>
     * without detail message.
     */
    public CompanyBeanValidaionException() {
    }

    /**
     * Constructs an instance of <code>CompanyBeanValidaionException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CompanyBeanValidaionException(String msg) {
        super(msg);
    }
}
