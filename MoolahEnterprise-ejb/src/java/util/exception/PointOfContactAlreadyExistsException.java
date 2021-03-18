/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author rayta
 */
public class PointOfContactAlreadyExistsException extends Exception {

    /**
     * Creates a new instance of
     * <code>PointOfContactAlreadyExistsException</code> without detail message.
     */
    public PointOfContactAlreadyExistsException() {
    }

    /**
     * Constructs an instance of
     * <code>PointOfContactAlreadyExistsException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public PointOfContactAlreadyExistsException(String msg) {
        super(msg);
    }
}
