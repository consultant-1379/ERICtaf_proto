package com.ericsson.cifwk.tdm.application.executions;

import com.ericsson.cifwk.tdm.api.Execution;
import org.jongo.Jongo;
import org.jongo.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.jongo.Oid.withOid;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 17/02/2016
 */
@Repository
public class ExecutionRepository {

    public static final String EXECUTIONS_COLLECTION = "executions";
    @Autowired
    Jongo jongo;

    public Execution insert(Execution execution) {
        jongo.getCollection(EXECUTIONS_COLLECTION).insert(execution);
        return execution;
    }

    public Execution update(Execution execution) {
        jongo.getCollection(EXECUTIONS_COLLECTION).save(execution);
        return execution;
    }

    public List<Execution> findAll() {
        MongoCursor<Execution> result = jongo.getCollection(EXECUTIONS_COLLECTION).find().as(Execution.class);
        List<Execution> executions = new ArrayList<>(result.count());
        result.forEach(executions::add);
        return executions;
    }

    public Execution findById(String executionId) {
        return jongo.getCollection(EXECUTIONS_COLLECTION).findOne(withOid(executionId)).as(Execution.class);
    }
}
