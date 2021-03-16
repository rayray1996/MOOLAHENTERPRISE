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
public class InvalidFilterCriteriaException extends Exception {

    /**
     * Creates a new instance of <code>InvalidFilterCriteriaException</code>
     * without detail message.
     */
    public InvalidFilterCriteriaException() {
    }

    /**
     * Constructs an instance of <code>InvalidFilterCriteriaException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidFilterCriteriaException(String msg) {
        super(msg);
    }
}
