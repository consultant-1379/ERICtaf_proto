/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.taf.hackathon.stackoverflow.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author esenofi
 */
@XmlRootElement(name = "response")
// Defining response as the root element so it and other elements can be mapped to the Response Class
public class LoginResponse {

    private String loginstatus;

    // Getters and Setters for the vAppList
    public void setLoginstatus(String loginstatus) {
        this.loginstatus = loginstatus;
    }

    @XmlElement(name = "login")
    public String getLoginstatus() {
        return this.loginstatus;
    }

}
