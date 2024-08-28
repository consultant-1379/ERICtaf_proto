package com.ericsson.cifwk.taf.grid.client;

import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface ClusterClient {

    void connect();

    String clientId();

    List<String> topology();

    void submitToGrid(String runId, Map<String, NodeInstruction> tasks);

    void disconnect();

    void waitUntilAllTestsFinished();

    MetricsService getMetricsService();

}
