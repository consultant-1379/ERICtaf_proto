/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.taf.hackathon.stackoverflow.data;

/**
 * @author esenofi
 *         This Class is used to catch exceptions thrown within the api
 */
public class StackOverflowServiceException extends Exception {
    // default contructor
    public StackOverflowServiceException() {
    }

    // contructor used to pass in the error message
    public StackOverflowServiceException(String error) {
        super(error);
    }

}
