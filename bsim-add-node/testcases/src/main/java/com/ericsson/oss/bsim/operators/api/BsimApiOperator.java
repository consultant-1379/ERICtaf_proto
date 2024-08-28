package com.ericsson.oss.bsim.operators.api;

import com.ericsson.cifwk.taf.ApiOperator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.cifwk.taf.osgi.client.ApiContainerClient;
import com.ericsson.cifwk.taf.osgi.client.ContainerNotReadyException;
import com.ericsson.cifwk.taf.osgi.client.JavaApi;
import com.ericsson.oss.bsim.getters.api.BsimApiGetter;
import org.apache.log4j.Logger;

import java.io.IOException;

public class BsimApiOperator implements ApiOperator {
    private static ApiClient client;
    private static Logger log = Logger.getLogger(BsimApiOperator.class);
    private static final Long CEX_START_TIME = 50000L;

    public static void prepareCex() throws ContainerNotReadyException, IOException {
        Host host = DataHandler.getHostByName("rc");
        ApiContainerClient osgiContainer = new ApiContainerClient(host, BsimApiGetter.CEX_SCRIPT, BsimApiGetter.CEX_CONFIG);
        if (!osgiContainer.canConnect()) {
            throw new IOException("Cannot connect to OSGi console");
        }
        int dataPort = osgiContainer.getDataPort();
        client = JavaApi.createApiClient("http://" + host.getIp() + ":" + dataPort + ApiContainerClient.AGENT_URI);
        if (!client.isAlive()) {
            log.info("Cannot start communication with the client. Will start the container now");
            osgiContainer.prepare(DataHandler.getAttribute("xdisplay").toString(), CEX_START_TIME);
        }
        if (!client.register(BsimApiGetter.getOsgiOperator()).getValue().equals("BsimOsgiOperator")) {
            throw new IOException("Cannot deploy OSGi remote part");
        }
    }

    public boolean addNode(String nodeName, String btsFqn) {
        return client.invoke("BsimOsgiOperator", "createAddNodeData", nodeName, btsFqn).getValue().contains(nodeName) && client.invoke("BsimOsgiOperator", "addNode").getValue().equals("???");
    }
}
