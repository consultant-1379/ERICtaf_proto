package com.ericsson.group.scenario.test.cases;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.annotatedMethod;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.flow;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;
import static com.ericsson.group.scenario.testSteps.ChargeCannon.CHARGE_CANNON;
import static com.ericsson.group.scenario.testSteps.ChargeCannon.PREPARE_CANNON;
import static com.ericsson.group.scenario.testSteps.FireCannon.FIRE_CANNON;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.ExecutionMode;
import com.ericsson.cifwk.taf.scenario.TestStepInvocation;
import com.ericsson.cifwk.taf.scenario.api.TestScenarioBuilder;
import com.ericsson.cifwk.taf.scenario.api.TestStepFlowBuilder;
import com.ericsson.group.scenario.testSteps.ChargeCannon;
import com.ericsson.group.scenario.testSteps.FireCannon;

public abstract class DeathStarOperationalManual extends TorTestCaseHelper {

    protected TestStepFlowBuilder instruction(String name,
            TestStepInvocation... steps) {
        TestStepFlowBuilder flow = flow("name");
        for (TestStepInvocation step : steps) {
            flow.addTestStep(step);
        }
        return flow;
    }

    protected int usingGenerators(int i) {
        return i;
    }

    static ChargeCannon chargeCannonTestSteps = new ChargeCannon();

    static FireCannon fireCannonTestSteps = new FireCannon();
    public static TestStepInvocation PREPARE_CANNON_STEP = annotatedMethod(
            chargeCannonTestSteps, PREPARE_CANNON);
    public static TestStepInvocation CHARGE_CANNON_STEP = annotatedMethod(
            chargeCannonTestSteps, CHARGE_CANNON);
    public static TestStepInvocation FIRE_CANNON_STEP = annotatedMethod(
            fireCannonTestSteps, FIRE_CANNON);

    public static boolean IN_SEQUENCE = false;
    public static boolean IN_PARALLEL = true;

    public static final int USE_ONE_GENERATOR = 1;
    public static final int USE_MULTIPLE_GENERATORS = 10;

    public boolean fireScenario(int vusers, boolean parallel,
            TestStepFlowBuilder... flows) {

        ExecutionMode mode = (parallel) ? ExecutionMode.PARALLEL
                : ExecutionMode.SEQUENTIAL;
        TestScenarioBuilder scenario = scenario().withVusers(vusers)
                .withExecutionMode(mode);
        for (TestStepFlowBuilder flow : flows) {
            scenario.addFlow(flow.build());
        }
        scenario.build().start();
        return false;
    }
}
