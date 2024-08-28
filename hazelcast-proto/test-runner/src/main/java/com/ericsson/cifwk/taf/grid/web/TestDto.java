package com.ericsson.cifwk.taf.grid.web;

import java.util.Date;

/**
 *
 */
public final class TestDto {

    public String runner;
    public String testware;
    public Step[] testSteps;

    public final static class Step {
        public String content;
        public int repeatCount;
        public Date from;
        public Date until;
        public long thinkTime;
        public int vUsers;
        public String key1;
        public String key2;
        public String key3;
        public String value1;
        public String value2;
        public String value3;
    }

}
