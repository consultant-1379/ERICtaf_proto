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
public class ErrorResponse {

    @XmlElement(name = "name")
    private String errorResp;

    // setters and getters for vapp_template_id
    public void setError(String e) {

        this.errorResp = e;
    }

    public String getError() {
        return errorResp;
    }

    @Override
    public String toString() {

        return errorResp;
    }

}
