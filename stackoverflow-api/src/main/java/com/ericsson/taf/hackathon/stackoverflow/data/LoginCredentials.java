package com.ericsson.taf.hackathon.stackoverflow.data;

/**
 * @author ekarran
 */
public class LoginCredentials {

    String username;

    String password;

    LoginCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUser() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
