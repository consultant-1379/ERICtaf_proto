package com.ericsson.taf.hackathon.stackoverflow.impl;

/**
 * Created by ekeimoo on 1/21/15.
 */
public class LogObject {
    private String title;
    private String url;

    public LogObject(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
