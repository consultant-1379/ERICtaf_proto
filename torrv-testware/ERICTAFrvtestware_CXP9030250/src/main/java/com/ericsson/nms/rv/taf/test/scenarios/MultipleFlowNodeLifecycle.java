package com.ericsson.nms.rv.taf.test.scenarios;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.scenario.ExecutionMode;
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
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.NodeTestSteps.SET_NODE_ATTRIBUTE;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.NodeTestSteps.CREATE_COLLECTION;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.NodeTestSteps.DELETE_COLLECTION;

public class MultipleFlowNodeLifecycle extends TorTestCaseHelper {

    @Inject
    private LauncherTestSteps launcherTestSteps;
    @Inject
    private NodeTestSteps nodeTestSteps;

    TestScenarioRunner runner;


    @TestId(id = "TEST3", title = "Multi-threaded test suites running different test steps")
    @Context(context = { Context.REST })
    @Test
    public void testThree() {
        Preconditions.checkNotNull(launcherTestSteps);
        Preconditions.checkNotNull(nodeTestSteps);
        runner = runner().build();

        TestStepFlow addNode = flow("Create Nodes")
                .addTestStep(annotatedMethod(launcherTestSteps, LOGIN))
                .addTestStep(annotatedMethod(nodeTestSteps, ADD_NODE))
                .build();

        TestStepFlow setAttribute = flow("Set node attribute and delete nodes")
                .withDataSources(dataSource("nodeAttributes"), dataSource("addedNode"))
                .addTestStep(annotatedMethod(nodeTestSteps, SET_NODE_ATTRIBUTE))
                .addTestStep(annotatedMethod(nodeTestSteps, DELETE_NODE))
                .build();

        TestStepFlow createSubscription = flow("Create subscriptions")
                .withDataSources(dataSource("create_sub"))
                .addTestStep(annotatedMethod(nodeTestSteps, CREATE_SUBSCRIPTION))
                .build();

        TestStepFlow deleteSubscription = flow("Delete subscriptions and nodes")
                .withDataSources(dataSource("AddedSubscriptions"), dataSource("addedNode"))
                .addTestStep(annotatedMethod(nodeTestSteps, DELETE_SUBSCRIPTION))
                .addTestStep(annotatedMethod(nodeTestSteps, DELETE_NODE))
                .build();

        TestStepFlow createCollection = flow("Create collection")
                .withDataSources(dataSource("collection"))
                .addTestStep(annotatedMethod(nodeTestSteps, CREATE_COLLECTION))
                .build();

        TestStepFlow deleteCollection = flow("Delete collection and nodes")
                .withDataSources(dataSource("addedNode"), dataSource("addedCollections"))
                .addTestStep(annotatedMethod(nodeTestSteps, DELETE_COLLECTION))
                .addTestStep(annotatedMethod(nodeTestSteps, DELETE_NODE))
                .build();


        TestStepFlow logout = flow("logout")
                .addTestStep(annotatedMethod(launcherTestSteps, LOGOUT))
                .build();

        TestScenario scenario = scenario()
                .withDataSources(
                        dataSource("users"), dataSource("node")
                )
                .withVusers(3)
                .withExecutionMode(ExecutionMode.PARALLEL)
                .addFlow(sequence(addNode, setAttribute, logout))
                .addFlow(sequence(addNode,createSubscription,deleteSubscription, logout))
                .addFlow(sequence(addNode, createCollection, deleteCollection, logout))
                .build();

        runner.start(scenario);
    }
}
