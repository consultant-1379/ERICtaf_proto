package com.ericsson.cifwk.taf.enm.test.scenarios;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
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
import static com.ericsson.cifwk.taf.enm.test.scenarios.testSteps.NodeTestSteps.*;

public class NodeLifecycleScenarioWithSynchronization extends TorTestCaseHelper {

    @Inject
    private LauncherTestSteps launcherTestSteps;
    @Inject
    private NodeTestSteps nodeTestSteps;



    @Test
    public void testTwo() {
        Preconditions.checkNotNull(launcherTestSteps);
        Preconditions.checkNotNull(nodeTestSteps);

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

      ExecutableScenario scenario = scenario()
                .withDataSources(
                        dataSource("node"),
                        dataSource("user")
                )
                .withVusers(2)
                .addFlow(sequence(createNodesScenario,deleteNodesScenario,logout))
                .addFlow(sequence(createSubscriptionsScenario,deleteSubscriptionScenario))
                .build();

        scenario.start();
    }
}
