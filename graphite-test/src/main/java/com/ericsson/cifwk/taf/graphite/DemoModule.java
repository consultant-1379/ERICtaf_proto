package com.ericsson.cifwk.taf.graphite;

import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class DemoModule extends AbstractModule {

    public static final String GRAPHITE_CONFIG = "graphite.properties";

    @Override
    protected void configure() {
        bind(ScheduledExecutorService.class)
                .annotatedWith(Names.named("report.scheduler"))
                .toInstance(Executors.newScheduledThreadPool(2));
        bind(CloseableHttpClient.class)
                .toProvider(new Provider<CloseableHttpClient>() {
                    @Override
                    public CloseableHttpClient get() {
                        return HttpClients.createDefault();
                    }
                });
        Properties properties = loadConfiguration();
        Names.bindProperties(binder(), properties);
    }

    private Properties loadConfiguration() {
        Properties properties = new Properties();
        URL resource = Resources.getResource(GRAPHITE_CONFIG);
        try (InputStream in = Resources.newInputStreamSupplier(resource).getInput()) {
            properties.load(in);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
        return properties;
    }

}
