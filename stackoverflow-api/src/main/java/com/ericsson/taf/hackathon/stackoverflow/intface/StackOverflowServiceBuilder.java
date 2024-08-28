package com.ericsson.taf.hackathon.stackoverflow.intface;

import com.ericsson.taf.hackathon.stackoverflow.impl.StackOverflowServiceImpl;

public class StackOverflowServiceBuilder {
    private static StackOverflowService comp;

    public static StackOverflowService newBuilder(String url, String username, String password) {
        if (comp == null || !(comp.getServer().equals(url) && comp.getUsername().equals(username) && comp.getPassword().equals(password))) {
            comp = new StackOverflowServiceImpl(url, username, password);
        }

        return comp;
    }
}
