package com.ericsson.cifwk.dashboard.application;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class DashboardConfiguration extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new DashboardServiceModule(),
                new DashboardServletModule()
        );
    }

}
