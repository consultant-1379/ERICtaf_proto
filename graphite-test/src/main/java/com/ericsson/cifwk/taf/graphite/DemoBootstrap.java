package com.ericsson.cifwk.taf.graphite;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class DemoBootstrap {

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new DemoModule());
        Demo demo = injector.getInstance(Demo.class);
        demo.start();
    }

}
