package com.ericsson.cifwk.taf.grid.client;

import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestSpecification;
import com.ericsson.cifwk.taf.grid.shared.TestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 *
 */
public final class DistributedTestRunner {

    private final Logger logger = LoggerFactory.getLogger(DistributedTestRunner.class);

    private ClusterClient clusterClient;

    public DistributedTestRunner(ClusterClient clusterClient) {
        this.clusterClient = clusterClient;
    }

    public void run(TestSpecification specification) {
        String testware = specification.getTestware();

        List<String> topology = clusterClient.topology();
        if (topology.isEmpty()) {
            throw new RuntimeException("No members in the test grid");
        }
        for (String host : topology) {
            logger.info("Found host : {}", host);
        }
        logger.info("Starting clustered test on number instances : {}", topology.size());

        TaskDistribution distribution = new TaskDistribution();
        String[] memberNames = topology.toArray(new String[]{});

        String clientId = clusterClient.clientId();
        TestStep[] testSteps = specification.getTestSteps();
        for (TestStep testStep : testSteps) {
            Map<String, NodeInstruction> tasks = distribution.distributeTasks(
                    specification,
                    testware,
                    memberNames,
                    clientId
            );
            clusterClient.submitToGrid(testStep.getId().toString(), tasks);
        }
    }

    public ClusterClient getClusterClient() {
        return clusterClient;
    }

}
