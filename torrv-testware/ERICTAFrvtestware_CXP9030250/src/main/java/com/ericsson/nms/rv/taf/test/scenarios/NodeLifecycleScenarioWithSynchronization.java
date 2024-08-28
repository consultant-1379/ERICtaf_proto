package com.ericsson.nms.rv.taf.test.scenarios;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
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
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.NodeTestSteps.CREATE_SUBSCRIPTION;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.NodeTestSteps.DELETE_NODE;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.NodeTestSteps.DELETE_SUBSCRIPTION;

public class NodeLifecycleScenarioWithSynchronization extends TorTestCaseHelper {

    @Inject
    private LauncherTestSteps launcherTestSteps;
    @Inject
    private NodeTestSteps nodeTestSteps;

    TestScenarioRunner runner;

    @Test
    public void testTwo() {
        Preconditions.checkNotNull(launcherTestSteps);
        Preconditions.checkNotNull(nodeTestSteps);
        runner = runner().build();

        TestStepFlow createNodesScenario = flow("Create Nodes")
                .withVusers(5)
                .addTestStep(annotatedMethod(launcherTestSteps, LOGIN))
                .addTestStep(annotatedMethod(nodeTestSteps, ADD_NODE))
                .build();

        TestStepFlow createSubscriptionsScenario = flow("Create subscriptions")
                .addTestStep(annotatedMethod(launcherTestSteps, LOGIN))
                .addTestStep(annotatedMethod(nodeTestSteps, CREATE_SUBSCRIPTION)).withDataSources(dataSource("create_sub"))
                .build();

        TestStepFlow deleteSubscriptionScenario = flow("Delete subscriptions")
                .addTestStep(annotatedMethod(nodeTestSteps, DELETE_SUBSCRIPTION)).withDataSources(dataSource("subscriptions"))
                .addTestStep(annotatedMethod(launcherTestSteps, LOGOUT))
                .build();

        TestStepFlow deleteNodesScenario = flow("Delete nodes")
                .withVusers(5)
                .addTestStep(annotatedMethod(nodeTestSteps, DELETE_NODE)).withDataSources(dataSource("addedNode"))
                .build();

        TestStepFlow logout = flow("logout")
                .withVusers(5)
                .addTestStep(annotatedMethod(launcherTestSteps, LOGOUT))
                .build();

        TestScenario scenario = scenario()
                .withDataSources(
                        dataSource("node"),
                        dataSource("user")
                )
                .withVusers(2)
                .addFlow(sequence(createNodesScenario,deleteNodesScenario,logout))
                .addFlow(sequence(createSubscriptionsScenario,deleteSubscriptionScenario))
                .build();

        runner.start(scenario);
    }
}
