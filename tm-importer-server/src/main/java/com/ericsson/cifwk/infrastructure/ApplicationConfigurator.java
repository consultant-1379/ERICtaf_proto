/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.infrastructure;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.core.Configurable;

public class ApplicationConfigurator {

    public <T extends Configurable<T>> T registerSharedFeatures(T configurable) {
        return configurable
                .register(MultiPartFeature.class)
                .register(JacksonFeature.class)
                .property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    }

}
