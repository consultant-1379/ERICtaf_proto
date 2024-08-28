package com.ericsson.cifwk.tdm;

import org.springframework.boot.SpringApplication;

/**
 * @author Alexey Nikolaenko alexey@tcherezov.com
 *         Date: 12/11/2015
 */
public final class ApplicationLauncher {

    private ApplicationLauncher() {
    }

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "dev");
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
}



