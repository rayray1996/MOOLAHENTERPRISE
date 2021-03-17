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
public class MonthlyPaymentNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>MonthlyPaymentNotFoundException</code>
     * without detail message.
     */
    public MonthlyPaymentNotFoundException() {
    }

    /**
     * Constructs an instance of <code>MonthlyPaymentNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public MonthlyPaymentNotFoundException(String msg) {
        super(msg);
    }
}
