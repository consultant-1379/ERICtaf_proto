package com.ericsson.abc;

import org.apache.log4j.Logger;

public class Main {

    static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        start();
    }

    public static void start() {
        LOG.info("ABC started");
        JMXThresholdMonitor.start();
        try {
            while (true) Thread.sleep(10000);
        } catch (InterruptedException ignore) {
        }
        stop();
    }

    public static void stop() {
        LOG.info("ABC ended");
        System.exit(0);
    }
}
