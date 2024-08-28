package com.ericsson.group.scenario.testSteps;

import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.TestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Operator
public class ChargeCannon {
    private final Logger logger = LoggerFactory.getLogger(ChargeCannon.class);

    public static final String PREPARE_CANNON = "prepareCannon";
    public static final String CHARGE_CANNON = "chargeCannon";

    @TestStep(id = PREPARE_CANNON)
    public void prepareCannon(){
        logger.info("Preparing cannon for firing");
    }

    @TestStep(id = CHARGE_CANNON)
    public void chargeCannon(){
        logger.info("Cannon is charging");
        logger.info("Charging complete: Prepare to fire");
    }
}
