package com.ericsson.oss.bsim.operators;

import java.io.IOException;

import com.ericsson.cifwk.taf.GenericOperator;
import com.ericsson.cifwk.taf.osgi.client.ContainerNotReadyException;
import com.ericsson.oss.bsim.operators.api.BsimApiOperator;

public class BsimOperator implements GenericOperator {

    public static void prepareCex() throws ContainerNotReadyException, IOException {
        BsimApiOperator.prepareCex();
    }

    BsimApiOperator bsim = new BsimApiOperator();

    public boolean addNode(String nodeName, String btsFqn) {
        return bsim.addNode(nodeName, btsFqn);
    }
}
