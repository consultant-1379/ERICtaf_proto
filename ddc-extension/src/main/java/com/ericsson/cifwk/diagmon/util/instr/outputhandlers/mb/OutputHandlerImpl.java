package com.ericsson.cifwk.diagmon.util.instr.outputhandlers.mb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ericsson.cifwk.diagmon.util.instr.OutputHandler;
import com.ericsson.cifwk.diagmon.util.instr.config.ConfigTreeNode;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelMonitoringDataEvent;
import com.ericsson.duraci.eiffelmessage.messages.events.Sender;

public class OutputHandlerImpl extends OutputHandler {

    private final Sender sender;
    private final String hostName;

    public OutputHandlerImpl(ConfigTreeNode nd) {
        String mbHost = nd.getAttribute("mb_host");
        if (mbHost == null)
            throw new NullPointerException("MessageBus host not specified");
        sender = new Sender(mbHost);
        String potentialHostName = nd.getAttribute("hostname");
        if (potentialHostName == null)
            try {
                potentialHostName = java.net.InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        hostName = potentialHostName;
    }

    private final Map<String, Long> previousValues = new HashMap<String, Long>();

    private void updateField(ConfigTreeNode node, String field) {
        Long value = Long.valueOf(node.getData());
        if (previousValues.get(field) != null) {
            Long newValue = value - previousValues.get(field);
            String newValueAsString = String.valueOf(newValue);
            node.addData(newValueAsString);
        }
        previousValues.put(field, value);
    }

    private void diffCpu(ConfigTreeNode node) {
        for (ConfigTreeNode data : node.getChildren()) {
            String field = data.baseName();
            updateField(data, field);
        }
    }

    @Override
    protected void recordValues(ConfigTreeNode node) {
        if (node.baseName().equals("Resources-OS-CPU")) {
            diffCpu(node);
        }
        EiffelMonitoringDataEvent event = EiffelMonitoringDataEvent.Factory.create(hostName, nodeToMap(node));
        sender.send(event);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Map nodeToMap(ConfigTreeNode node) {
        Map result = new HashMap();
        Object data = null;
        if (node.getData() == null) {
            data = new ArrayList();
            for (ConfigTreeNode subNode : node.getChildren()) {
                ((List<Map>) data).add(nodeToMap(subNode));
            }
        } else {
            data = node.getData();
        }
        result.put(node.baseName(), data);
        return result;
    }

    @Override
    public void shutdown() {
        sender.dispose();

    }

}
