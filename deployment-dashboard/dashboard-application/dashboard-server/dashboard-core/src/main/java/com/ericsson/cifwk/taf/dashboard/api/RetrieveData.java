package com.ericsson.cifwk.taf.dashboard.api;

import com.ericsson.cifwk.taf.dashboard.api.dto.DeploymentRequest;
import com.ericsson.cifwk.taf.dashboard.api.dto.HostServices;
import com.ericsson.cifwk.taf.dashboard.api.dto.ISOVersion;
import com.ericsson.cifwk.taf.dashboard.api.dto.Node;
import com.ericsson.cifwk.taf.dashboard.api.dto.NodeServices;
import com.ericsson.cifwk.taf.dashboard.infrastructure.mapping.NodeServicesMapper;
import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.io.IOException;

/**
 * Created by egergle on 10/12/2015.
 */
public class RetrieveData {
    public static Logger logger = Logger.getLogger(RetrieveData.class);

    public static Thread getNodeServices(final SseEmitter sseEmitter, final DeploymentRequest deploymentRequest, final ThreadGroup threadGroup, final Long timeout) {
        Thread thread = new Thread(threadGroup, "nodeServices-" + deploymentRequest.getUid()) {
            public void run() {
                while (true) {
                    try {
                        NodeServices nodeServices = new NodeServices();
                        NodeServicesMapper nodeServicesMapper = new NodeServicesMapper();

                        nodeServices.setServices(nodeServicesMapper.map(DeploymentInfo.retrieveStatusOfServices(deploymentRequest.getClusterId())));
                        nodeServices.setType(NodeServices.TYPE);
                        sseEmitter.send(nodeServices);
                        sleep(timeout);
                    } catch (IOException | InterruptedException e) {
                        logger.error(e.getMessage());
                        return;
                    }
                }
            }
        };

        thread.start();
        return thread;
    }

    public static Thread getNodeVersion(final SseEmitter sseEmitter, final DeploymentRequest deploymentRequest, final ThreadGroup threadGroup, final Long timeout) {
        Thread thread = new Thread(threadGroup, "nodeVersion-" + deploymentRequest.getUid()) {
            public void run() {
                while (true) {
                    try {
                        ISOVersion iso = new ISOVersion();
                        iso.setVersion(DeploymentInfo.getENMVersion(deploymentRequest.getClusterId()));
                        iso.setType(ISOVersion.TYPE);
                        sseEmitter.send(iso);
                        sleep(timeout);
                    } catch (IOException | InterruptedException e) {
                        logger.error(e.getMessage());
                        return;
                    }
                }
            }
        };

        thread.start();
        return thread;
    }

    public static Thread getPingableNodes(final SseEmitter sseEmitter, final DeploymentRequest deploymentRequest, final ThreadGroup threadGroup, final Long timeout) {
        Thread thread = new Thread(threadGroup, "pingNodes-" + deploymentRequest.getUid()) {
            public void run() {
                while (true) {
                    try {
                        HostServices hostServices = new HostServices();
                        hostServices.setServices(DeploymentInfo.retrieveStatusOfHosts(deploymentRequest.getClusterId()));
                        hostServices.setType(HostServices.TYPE);
                        sseEmitter.send(hostServices);
                        sleep(timeout);
                    } catch (IOException | InterruptedException e) {
                        logger.error(e.getMessage());
                        return;
                    }
                }
            }
        };

        thread.start();
        return thread;
    }
}
