package com.ericsson.cifwk.taf.grid.client;

import com.ericsson.cifwk.taf.grid.Configurations;
import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.grid.shared.TestUpdate;
import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.configuration.Configuration;
import org.jgroups.*;
import org.jgroups.blocks.MessageDispatcher;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.blocks.RequestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class JGroupsClusterClient extends ReceiverAdapter implements ClusterClient, RequestHandler {

    public static final String CLUSTER_NAME = "TafCluster";

    private final Logger logger = LoggerFactory.getLogger(JGroupsClusterClient.class);

    private final AtomicInteger testsToRun = new AtomicInteger();
    private final MetricsService metricsService;
    private final Configuration configuration;
    private JChannel channel;
    private MessageDispatcher dispatcher;

    JGroupsClusterClient(MetricsService metricsService, Configuration configuration) {
        this.metricsService = metricsService;
        this.configuration = configuration;
    }

    @Override
    public void connect() {
        try {
            doConnect();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private void doConnect() throws Exception {
        String properties = configuration.getString(Configurations.JGROUPS_PROPS);
        channel = new JChannel(properties);
        dispatcher = new MessageDispatcher(channel, this, this, this);
        channel.connect(CLUSTER_NAME);
    }

    @Override
    public String clientId() {
        Address address = channel.getAddress();
        return address.toString();
    }

    @Override
    public List<String> topology() {
        View view = channel.getView();
        List<Address> members = view.getMembers();
        Iterable<String> hosts = Iterables.transform(members, new Function<Address, String>() {
            @Override
            public String apply(Address input) {
                return input.toString();
            }
        });
        List<String> result = Lists.newArrayList(hosts);
        result.remove(clientId());
        return result;
    }

    @Override
    public void submitToGrid(String runId, Map<String, NodeInstruction> tasks) {
        for (Map.Entry<String, NodeInstruction> entry : tasks.entrySet()) {
            String key = entry.getKey();
            NodeInstruction instruction = entry.getValue();

            Address destination = addressByKey(key);
            Message message = new Message(destination, instruction);
            logger.info("Sending message to {}", key);
            try {
                dispatcher.sendMessageWithFuture(message, RequestOptions.ASYNC());
                TestSchedule schedule = instruction.getSchedule();
                testsToRun.addAndGet(schedule.getVusers() * schedule.getRepeatCount());
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
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
    public Object handle(Message message) throws Exception {
        logger.info("Message received");
        receive(message);
        return "OK";
    }

    @Override
    public void receive(Message message) {
        Object object = message.getObject();
        if (object instanceof TestUpdate) {
            testsToRun.decrementAndGet();
            metricsService.updateMetrics((TestUpdate) object);
        } else {
            throw new RuntimeException("Received wrong event type : " + object.getClass().getSimpleName());
        }
    }

    @Override
    public void waitUntilAllTestsFinished() {
        while (testsToRun.get() != 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void disconnect() {
        if (channel != null) {
            channel.disconnect();
            dispatcher.stop();
        }
    }

    @Override
    public MetricsService getMetricsService() {
        return metricsService;
    }
}
