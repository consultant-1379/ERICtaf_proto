package com.ericsson.taf.hackathon.stackoverflow.intface;

import com.ericsson.taf.hackathon.stackoverflow.data.StackOverflowServiceException;

/**
 * StackOverflowService is an Interface thats holds methods used by any class that
 * implements it.
 */

public interface StackOverflowService {

    /*
     * Gets the server url that has been passed to the contructor
     * @return - returns the username
     */
    String getServer();

    /*
     * Gets the user name that has been passed to the contructor
     * @return - returns the username
     */
    String getUsername();

    /*
     * Gets the password that has been passed to the contructor
     * @return - returns the password
     */
    String getPassword();

    String login() throws StackOverflowServiceException;

    String getAnswer(String query);

}
