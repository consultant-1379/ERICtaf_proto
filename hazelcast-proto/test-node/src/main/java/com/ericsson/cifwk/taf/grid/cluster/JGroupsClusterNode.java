package com.ericsson.cifwk.taf.grid.cluster;

import com.ericsson.cifwk.taf.grid.engine.ThreadPoolTestEngine;
import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestEngine;
import com.ericsson.cifwk.taf.grid.shared.TestUpdate;
import com.google.common.base.Throwables;
import org.apache.commons.configuration.Configuration;
import org.jgroups.*;
import org.jgroups.blocks.MessageDispatcher;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.blocks.RequestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public final class JGroupsClusterNode extends ReceiverAdapter implements ClusterNode, ResultReporter, RequestHandler {

    public static final String CLUSTER_NAME = "TafCluster";

    private final Logger logger = LoggerFactory.getLogger(JGroupsClusterNode.class);

    private final Configuration configuration;
    private MessageDispatcher dispatcher;
    private JChannel channel;

    JGroupsClusterNode(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void start() throws Exception {
        channel = new JChannel();
        dispatcher = new MessageDispatcher(channel, this, this, this);
        channel.connect(CLUSTER_NAME);
    }

    @Override
    public void publish(NodeInstruction instruction, TestUpdate update) {
        try {
            doPublish(instruction, update);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private void doPublish(NodeInstruction instruction, TestUpdate update) throws Exception {
        String clientId = instruction.getClientId();
        Address address = addressByKey(clientId);
        Message message = new Message(address, update);
        dispatcher.sendMessage(message, RequestOptions.ASYNC());
    }

    private Address addressByKey(String key) {
        View view = channel.getView();
        List<Address> members = view.getMembers();
        for (Address member : members) {
            if (member.toString().equals(key)) {
                return member;
            }
        }
        throw new IllegalArgumentException("Address not found : " + key);
    }

    @Override
    public Object handle(Message msg) throws Exception {
        logger.info("Message received");
        receive(msg);
        return "OK";
    }

    @Override
    public void receive(Message message) {
        Object object = message.getObject();
        if (object instanceof NodeInstruction) {
            try {
                executeInstruction((NodeInstruction) object);
            } catch (Exception e) {
                logger.error("Failed to execute", e);
            }
        } else {
            throw new RuntimeException("Received wrong event type : " + object.getClass().getSimpleName());
        }
    }

    private void executeInstruction(NodeInstruction instruction) {
        TestEngine testEngine = new ThreadPoolTestEngine(this);
        testEngine.exec(instruction);
    }

    @Override
    public void shutdown() {
        if (channel != null) {
            channel.close();
            dispatcher.stop();
        }
    }

}
