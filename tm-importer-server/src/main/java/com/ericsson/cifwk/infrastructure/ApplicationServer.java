package com.ericsson.cifwk.infrastructure;

import com.google.common.io.Resources;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Main class.
 */
public class ApplicationServer {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "base_uri";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.ericsson.cifwk package
        final ResourceConfig rc = new ResourceConfig()
                .register(MultiPartFeature.class)
                .packages("com.ericsson.cifwk");

        // uncomment the following line if you want to enable
        // support for JSON on the service (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml)
        // --
        // rc.addBinder(org.glassfish.jersey.media.json.JsonJaxbBinder);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI

        String baseURI = PropertyLoader.getProperty(BASE_URI).toString();
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(baseURI), rc);
    }

    /**
     * Main method.
     *
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        PropertyLoader.getProperties();
        final HttpServer server = startServer();

        URL resource = Resources.getResource("content/");
        try {
            server.getServerConfiguration().addHttpHandler(
                    new StaticHttpHandler(resource.toURI().getPath().toString()), "/main");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", PropertyLoader.getProperty(BASE_URI).toString()));
        System.in.read();
    }
}

