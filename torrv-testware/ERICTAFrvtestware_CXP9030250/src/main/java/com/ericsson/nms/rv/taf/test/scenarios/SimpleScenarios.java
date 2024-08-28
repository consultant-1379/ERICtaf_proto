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

import static com.ericsson.cifwk.taf.scenario.TestScenarios.annotatedMethod;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.dataSource;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.flow;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.LauncherTestSteps.LOGIN;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.LauncherTestSteps.LOGOUT;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.NodeTestSteps.ADD_NODE;
import static com.ericsson.nms.rv.taf.test.scenarios.testSteps.NodeTestSteps.DELETE_NODE;

public class SimpleScenarios extends TorTestCaseHelper{

    @Inject
    private LauncherTestSteps launcher;
    @Inject
    private NodeTestSteps nodeOperator;

    TestScenarioRunner runner;

    @Context(context = { Context.REST })
    @Test
    public void verifyCommonScenarioWithMultipleVUsers() {
        Preconditions.checkNotNull(launcher);
        Preconditions.checkNotNull(nodeOperator);
        runner = runner().build();

        TestStepFlow login = flow("Login").addTestStep(annotatedMethod(launcher, LOGIN)).build();

        TestStepFlow addNodes = flow(
                "Create nodes")
                .addTestStep(annotatedMethod(nodeOperator, ADD_NODE))
                .build();


        TestStepFlow delete = flow("Delete Node").addTestStep(annotatedMethod(nodeOperator, DELETE_NODE)).withDataSources(dataSource("addedNode")).build();
        TestStepFlow logout = flow("Logout").addTestStep(annotatedMethod(launcher, LOGOUT))
                .build();

        TestScenario scenario = scenario()
                .withDataSources(dataSource("node"), dataSource("user"))
                .addFlow(login).withVusers(1)
                .build();

        runner.start(scenario);

        TestScenario multipleFlowScenario = scenario()
                .withDataSources(dataSource("node"), dataSource("user"))
                .addFlow(login)
                .addFlow(addNodes)
                .addFlow(delete)
                .addFlow(logout)
                .withVusers(1)
                .build();
        runner.start(multipleFlowScenario);
    }
}
