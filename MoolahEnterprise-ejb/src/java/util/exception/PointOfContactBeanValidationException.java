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
public class PointOfContactBeanValidationException extends Exception {

    /**
     * Creates a new instance of
     * <code>PointOfContactBeanValidationException</code> without detail
     * message.
     */
    public PointOfContactBeanValidationException() {
    }

    /**
     * Constructs an instance of
     * <code>PointOfContactBeanValidationException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public PointOfContactBeanValidationException(String msg) {
        super(msg);
    }
}
