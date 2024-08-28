package com.ericsson.cifwk.taf.grid.client;

import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.grid.shared.TestSpecification;
import com.ericsson.cifwk.taf.grid.shared.TestStep;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 *
 */
public final class TaskDistribution {

    public Map<String, NodeInstruction> distributeTasks(TestSpecification specification,
                                                        String classpath,
                                                        String[] memberNames,
                                                        String clientId) {
        Map<String, NodeInstruction> tasks = Maps.newHashMap();

        String runner = specification.getRunner();
        String container = specification.getContainer();
        TestStep[] testSteps = specification.getTestSteps();

        for (TestStep testStep : testSteps) {
            Map<String, String> attributes = testStep.getAttributes();
            String[] stepNodes = testStep.getMembers();

            if (stepNodes == null || stepNodes.length == 0) {
                stepNodes = memberNames;
            }

            TestSchedule schedule = testStep.getSchedule();

            Map<String, Integer> loadDistribution = distributeLoad(stepNodes, schedule.getVusers());

            for (String memberId : stepNodes) {
                NodeInstruction instruction = new NodeInstruction();
                instruction.setId(testStep.getId());
                instruction.setClientId(clientId);
                Integer nodeVusers = loadDistribution.get(memberId);
                instruction.setSchedule(schedule.copy(nodeVusers));
                instruction.setClasspath(classpath);
                instruction.setContainer(container);
                instruction.setRunner(runner);
                instruction.setAttributes(attributes);
                tasks.put(memberId, instruction);
            }
        }

        return tasks;
    }

    private Map<String, Integer> distributeLoad(String[] nodes, int vusers) {
        int clusterSize = nodes.length;
        int quotient = vusers / clusterSize;
        int remainder = vusers % clusterSize;

        Map<String, Integer> result = Maps.newHashMap();
        for (int i = 0; i < nodes.length; i++) {
            String node = nodes[i];
            if (i < nodes.length - 1) {
                result.put(node, quotient);
            } else {
                result.put(node, quotient + remainder);
            }
        }

        return result;
    }


}
