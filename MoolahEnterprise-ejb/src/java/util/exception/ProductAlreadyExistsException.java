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
public class ProductAlreadyExistsException extends Exception {

    /**
     * Creates a new instance of <code>ProductAlreadyExistsException</code>
     * without detail message.
     */
    public ProductAlreadyExistsException() {
    }

    /**
     * Constructs an instance of <code>ProductAlreadyExistsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ProductAlreadyExistsException(String msg) {
        super(msg);
    }
}
