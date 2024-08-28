package com.ericsson.cifwk.taf.graphite;

import com.google.common.base.Throwables;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GraphiteClient {

    private final Logger logger = LoggerFactory.getLogger(GraphiteClient.class);

    private final String host;
    private final int port;
    private final CloseableHttpClient httpClient;

    @Inject
    public GraphiteClient(@Named("graphite.host") String host,
                          @Named("graphite.port") int port,
                          CloseableHttpClient httpClient) {
        this.host = host;
        this.port = port;
        this.httpClient = httpClient;
    }

    public String get(String format, String target, String from) throws IOException {
        URI uri = getURI(format, target, from);
        HttpGet request = new HttpGet(uri);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        }
    }

    private URI getURI(String format, String target, String from) {
        try {
            return new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPort(port)
                    .setPath("/render")
                    .addParameter("format", format)
                    .addParameter("target", target)
                    .addParameter("from", from)
                    .build();
        } catch (URISyntaxException e) {
            throw Throwables.propagate(e);
        }
    }

}
