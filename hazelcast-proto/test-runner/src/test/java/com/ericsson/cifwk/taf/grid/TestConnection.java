package com.ericsson.cifwk.taf.grid;

import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.grid.shared.TestSpecification;
import com.ericsson.cifwk.taf.grid.shared.TestStep;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 *
 */
public class TestConnection {

    public static void main(String[] args) {
        TestSpecification specification = new TestSpecification();
        specification.setRunner("runner");
        specification.setContainer("container");
        specification.setTestware("testware");
        TestSchedule schedule = new TestSchedule();
        schedule.setRepeatCount(100);
        schedule.setVusers(10);
        schedule.setFrom(1L);
        schedule.setUntil(100L);
        schedule.setThinkTime(1L);
        TestStep testStep = new TestStep(new String[] {"a", "b"}, schedule, Maps.<String,String>newHashMap());
        specification.setTestSteps(new TestStep[] {testStep} );

        Gson gson = new Gson();
        String json = gson.toJson(specification);
        System.out.println(json);
    }

}
