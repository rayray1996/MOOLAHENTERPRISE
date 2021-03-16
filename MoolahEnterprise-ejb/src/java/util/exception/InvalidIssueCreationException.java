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
public class InvalidIssueCreationException extends Exception {

    /**
     * Creates a new instance of <code>InvalidIssueCreationException</code>
     * without detail message.
     */
    public InvalidIssueCreationException() {
    }

    /**
     * Constructs an instance of <code>InvalidIssueCreationException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidIssueCreationException(String msg) {
        super(msg);
    }
}
