package com.ericsson.cifwk.ve.application;

public class Dashboard {

    private final DashboardConfiguration configuration;

    public Dashboard(DashboardConfiguration configuration) {
        this.configuration = configuration;
    }

    public void start() {
        final Bootstrap bootstrap = Bootstrap.getInstance();
        bootstrap.init(configuration);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                bootstrap.shutdown();
            }
        });
    }
}
