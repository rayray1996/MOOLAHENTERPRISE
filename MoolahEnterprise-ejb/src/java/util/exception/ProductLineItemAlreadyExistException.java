/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author nickg
 */
public class ProductLineItemAlreadyExistException extends Exception {


    /**
     * Constructs an instance of
     * <code>ProductLineItemAlreadyExistException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ProductLineItemAlreadyExistException(String msg) {
        super(msg);
    }
}
