package com.ericsson.cifwk.taf.enm.test.scenarios;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.LauncherTestSteps;
import com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.NodeTestSteps;
import com.ericsson.cifwk.taf.scenario.ExecutableScenario;
import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.google.common.base.Preconditions;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;
import static com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.LauncherTestSteps.LOGIN;
import static com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.LauncherTestSteps.LOGOUT;
import static com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.NodeTestSteps.ADD_NODE;
import static com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.NodeTestSteps.DELETE_NODE;

public class SimpleScenarios extends TorTestCaseHelper{

    @Inject
    private LauncherTestSteps launcher;
    @Inject
    private NodeTestSteps nodeOperator;

    @Context(context = { Context.REST })
    @Test
    public void verifyCommonScenarioWithMultipleVUsers() {
        Preconditions.checkNotNull(launcher);
        Preconditions.checkNotNull(nodeOperator);

        TestStepFlow login = flow("Login").addTestStep(annotatedMethod(launcher, LOGIN)).build();

        TestStepFlow addNodes = flow(
                "Create nodes")
                .addTestStep(annotatedMethod(nodeOperator, ADD_NODE))
                .build();


        TestStepFlow delete = flow("Delete Node").addTestStep(annotatedMethod(nodeOperator, DELETE_NODE)).build();
        TestStepFlow logout = flow("Logout").addTestStep(annotatedMethod(launcher, LOGOUT))
                .build();

        ExecutableScenario scenario = scenario()
                .withDataSources(dataSource("node"), dataSource("user"))
                .addFlow(login).withVusers(1)
                .build();

        scenario.start();

        ExecutableScenario multipleFlowScenario = scenario()
                .withDataSources(dataSource("node"), dataSource("user"))
                .addFlow(login).withVusers(1)
                .addFlow(addNodes).withVusers(1)
                .addFlow(delete).withVusers(1)
                .build();
        multipleFlowScenario.start();
    }
}
