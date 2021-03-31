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
public class AssetEntityDoesNotExistException extends Exception {

    /**
     * Creates a new instance of <code>AssetEntityDoesNotExistException</code>
     * without detail message.
     */
    public AssetEntityDoesNotExistException() {
    }

    /**
     * Constructs an instance of <code>AssetEntityDoesNotExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AssetEntityDoesNotExistException(String msg) {
        super(msg);
    }
}
