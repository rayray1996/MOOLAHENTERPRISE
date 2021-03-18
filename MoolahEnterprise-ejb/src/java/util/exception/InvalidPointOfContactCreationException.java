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
public class InvalidPointOfContactCreationException extends Exception {

    /**
     * Creates a new instance of
     * <code>InvalidPointOfContactCreationException</code> without detail
     * message.
     */
    public InvalidPointOfContactCreationException() {
    }

    /**
     * Constructs an instance of
     * <code>InvalidPointOfContactCreationException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public InvalidPointOfContactCreationException(String msg) {
        super(msg);
    }
}
