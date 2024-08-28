package com.ericsson.cifwk.taf.enm.test.scenarios;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.scenario.ExecutableScenario;
import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.LauncherTestSteps;
import com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.NodeTestSteps;
import com.google.common.base.Preconditions;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;
import static com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.LauncherTestSteps.LOGIN;
import static com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.LauncherTestSteps.LOGOUT;
import static com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.NodeTestSteps.ADD_NODE;
import static com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.NodeTestSteps.DELETE_NODE;

public class NodeLifecycleScenarioTwoVusers extends TorTestCaseHelper {

    @Inject
    private LauncherTestSteps launcherTestSteps;
    @Inject
    private NodeTestSteps nodeTestSteps;

    @Context(context = { Context.REST })
    @Test
    public void verifyCommonScenarioWithMultipleVUsers() {
        Preconditions.checkNotNull(launcherTestSteps);
        Preconditions.checkNotNull(nodeTestSteps);

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

        ExecutableScenario scenario = scenario()
                .withDataSources(dataSource("node"), dataSource("user"))
                .withVusers(2)
                .addFlow(sequence(createNodesScenario,deleteNodesScenario,logout))
                .build();

        scenario.start();
    }
}
