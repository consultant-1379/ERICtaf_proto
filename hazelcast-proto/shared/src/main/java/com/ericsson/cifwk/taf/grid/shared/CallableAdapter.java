package com.ericsson.cifwk.taf.grid.shared;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;


/**
 *
 */
public final class CallableAdapter implements Callable<Object>, HazelcastInstanceAware, Serializable {

    private final NodeInstruction instruction;
    private HazelcastInstance hazelcast;

    public CallableAdapter(NodeInstruction instruction) {
        this.instruction = instruction;
    }

    @Override
    public Object call() throws Exception {
        ConcurrentMap<String, Object> userContext = hazelcast.getUserContext();
        TestEngine testEngine = (TestEngine) userContext.get(TestEngine.class.getName());
        testEngine.exec(instruction);
        return "OK";
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        hazelcast = hazelcastInstance;
    }

}
