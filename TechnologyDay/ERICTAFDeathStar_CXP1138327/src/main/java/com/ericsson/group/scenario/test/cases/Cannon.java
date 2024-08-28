package com.ericsson.group.scenario.test.cases;

import org.testng.annotations.Test;

public class Cannon extends DeathStarOperationalManual {

    @Test
    public void cannonPerformanceTest() {

        fireScenario(
                USE_ONE_GENERATOR,
                IN_SEQUENCE,
                instruction("Charge Cannon", PREPARE_CANNON_STEP,
                        CHARGE_CANNON_STEP),
                instruction("Fire Cannon", FIRE_CANNON_STEP));

        // TestStepFlow chargeCannon = flow("Charge Cannon")
        // .addTestStep(
        // annotatedMethod(chargeCannonTestSteps, PREPARE_CANNON))
        // .addTestStep(
        // annotatedMethod(chargeCannonTestSteps, CHARGE_CANNON))
        // .build();
        //
        // TestStepFlow fireCannon = flow("Fire Cannon").addTestStep(
        // annotatedMethod(fireCannonTestSteps, FIRE_CANNON)).build();
        //
        // ExecutableScenario fireCannonScenario = scenario().withVusers(1)
        // .withExecutionMode(ExecutionMode.PARALLEL)
        // .addFlow(chargeCannon).addFlow(fireCannon).build();
        //
        // fireCannonScenario.start();

    }
}
