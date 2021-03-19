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
public class RefundErrorException extends Exception {


    /**
     * Constructs an instance of <code>RefundErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RefundErrorException(String msg) {
        super(msg);
    }
}
