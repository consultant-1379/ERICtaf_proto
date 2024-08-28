package com.ericsson.cifwk.tdm.application.executions;

import com.ericsson.cifwk.tdm.api.DataSourceExecution;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.jongo.Oid.withOid;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 22/02/2016
 */
@Repository
public class DataSourceExecutionRepository {

    public static final String DATA_SOURCE_EXECUTIONS_COLLECTION = "dataSourceExecutions";
    @Autowired
    Jongo jongo;

    public DataSourceExecution insert(DataSourceExecution execution) {
        jongo.getCollection(DATA_SOURCE_EXECUTIONS_COLLECTION).insert(execution);
        return execution;
    }

    public DataSourceExecution update(DataSourceExecution execution) {
        jongo.getCollection(DATA_SOURCE_EXECUTIONS_COLLECTION).save(execution);
        return execution;
    }

    public List<DataSourceExecution> findAll() {
        MongoCursor<DataSourceExecution> result = jongo.getCollection(DATA_SOURCE_EXECUTIONS_COLLECTION).find().as(DataSourceExecution.class);
        List<DataSourceExecution> executions = new ArrayList<>(result.count());
        result.forEach(executions::add);
        return executions;
    }

    public DataSourceExecution findById(String executionId) {
        return jongo.getCollection(DATA_SOURCE_EXECUTIONS_COLLECTION).findOne(withOid(executionId)).as(DataSourceExecution.class);
    }

    public void delete(DataSourceExecution executionRecord) {
        jongo.getCollection(DATA_SOURCE_EXECUTIONS_COLLECTION).remove(new ObjectId(executionRecord.id));
    }

    public List<DataSourceExecution> findByExecutionId(String executionId) {
        MongoCursor<DataSourceExecution> result = jongo.getCollection(DATA_SOURCE_EXECUTIONS_COLLECTION)
                .find("{executionId:#}", executionId)
                .as(DataSourceExecution.class);

        List<DataSourceExecution> dataRecords = new ArrayList<>(result.count());
        result.forEach(dataRecords::add);
        return dataRecords;
    }
}
