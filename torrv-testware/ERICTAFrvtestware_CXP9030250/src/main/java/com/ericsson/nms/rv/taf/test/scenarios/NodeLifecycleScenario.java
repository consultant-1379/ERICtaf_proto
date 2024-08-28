package com.ericsson.nms.rv.taf.test.scenarios;


import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.nms.rv.taf.test.scenarios.testSteps.LauncherTestSteps;
import com.ericsson.nms.rv.taf.test.scenarios.testSteps.NodeTestSteps;
import com.google.common.base.Preconditions;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.LauncherTestSteps.LOGIN;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.LauncherTestSteps.LOGOUT;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.NodeTestSteps.ADD_NODE;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.NodeTestSteps.DELETE_NODE;

public class NodeLifecycleScenario extends TorTestCaseHelper {

    @Inject
    private LauncherTestSteps launcherTestSteps;
    @Inject
    private NodeTestSteps nodeTestSteps;

    TestScenarioRunner runner;

    @Context(context = { Context.REST })
    @Test
    public void verifyCommonScenarioWithMultipleVUsers() {
        Preconditions.checkNotNull(launcherTestSteps);
        Preconditions.checkNotNull(nodeTestSteps);
        runner = runner().build();

        TestStepFlow createNodesScenario = flow("Create Nodes")
                .addTestStep(annotatedMethod(launcherTestSteps, LOGIN))
                .addTestStep(annotatedMethod(nodeTestSteps, ADD_NODE))
                .build();

        TestStepFlow deleteNodesScenario = flow("Delete nodes")
                .addTestStep(annotatedMethod(nodeTestSteps, DELETE_NODE)).withDataSources(dataSource("addedNode"))
                .build();

        TestStepFlow logout = flow("logout")
                .addTestStep(annotatedMethod(launcherTestSteps, LOGOUT))
                .build();

        TestScenario scenario = scenario()
                .withDataSources(dataSource("node"), dataSource("user"))
                .addFlow(sequence(createNodesScenario, deleteNodesScenario, logout))
                .build();

        runner.start(scenario);
    }

}
