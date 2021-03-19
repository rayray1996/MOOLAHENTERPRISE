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
public class CompanySQLConstraintException extends Exception {

    /**
     * Creates a new instance of <code>CompanySQLConstraintException</code>
     * without detail message.
     */
    public CompanySQLConstraintException() {
    }

    /**
     * Constructs an instance of <code>CompanySQLConstraintException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CompanySQLConstraintException(String msg) {
        super(msg);
    }
}
