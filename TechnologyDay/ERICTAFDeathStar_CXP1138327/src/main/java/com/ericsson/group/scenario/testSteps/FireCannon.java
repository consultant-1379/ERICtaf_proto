package com.ericsson.group.scenario.testSteps;

import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.TestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Operator
public class FireCannon {
    private final Logger logger = LoggerFactory.getLogger(FireCannon.class);

    public static final String FIRE_CANNON = "fireCannon";

    @TestStep(id = FIRE_CANNON)
    public void fireCannon(){
        logger.info("Cannon is firing");
    }
}
