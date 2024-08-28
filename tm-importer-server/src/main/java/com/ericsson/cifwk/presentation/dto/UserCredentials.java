package com.ericsson.cifwk.presentation.dto;

/**
 *
 */
public class UserCredentials {

    private String username;

    private String password;

    public UserCredentials() {
    }

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
