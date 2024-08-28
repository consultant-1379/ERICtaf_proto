package com.ericsson.cifwk.tdm.presentation.controllers.exceptions;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 22/02/2016
 */
public class LockException extends RuntimeException {
    public LockException(String message) {
        super(message);
    }
}
