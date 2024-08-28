/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.infrastructure;

import com.ericsson.cifwk.presentation.dto.UserCredentials;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestClient {

    private WebTarget target;

    public RestClient(String url) {
        ApplicationConfigurator configurator = new ApplicationConfigurator();
        Client client = configurator
                .registerSharedFeatures(ClientBuilder.newBuilder())
                .build();

        client.register(new CookieFilter());

        target = client.target(url);
    }

    public WebTarget path(String path) {
        return target.path(path);
    }

    public Response login(String user, String password) {
        return loginRequest().post(Entity.entity(new UserCredentials(user, password), MediaType.APPLICATION_JSON));
    }

    public Response logout() {
        return loginRequest().delete();
    }

    private Invocation.Builder loginRequest() {
        return path("/tm-server/api/login").request();
    }

    @Provider
    private static class CookieFilter implements ClientRequestFilter, ClientResponseFilter{

        private Map<String, NewCookie> cookiesMap = new HashMap<String, NewCookie>();

        @Override
         public void filter(ClientRequestContext requestContext) throws IOException {
            // improvement: filter out expired cookies
            requestContext.getHeaders().put("Cookie", new ArrayList<Object>(cookiesMap.values()));
        }

        @Override
        public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
            if (responseContext.getCookies() != null) {
                cookiesMap.putAll(responseContext.getCookies());
            }
        }
    }
}
