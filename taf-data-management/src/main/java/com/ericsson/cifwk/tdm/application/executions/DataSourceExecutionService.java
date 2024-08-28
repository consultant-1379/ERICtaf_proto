package com.ericsson.cifwk.tdm.application.executions;

import com.ericsson.cifwk.tdm.api.DataRecord;
import com.ericsson.cifwk.tdm.api.DataSourceExecution;
import com.ericsson.cifwk.tdm.api.DataSourceIdentity;
import com.ericsson.cifwk.tdm.api.Execution;
import com.ericsson.cifwk.tdm.application.datasources.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 22/02/2016
 */
@Service
public class DataSourceExecutionService {

    @Autowired
    DataSourceExecutionRepository dataSourceExecutionRepository;

    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    ExecutionRepository executionRepository;

    public DataSourceExecution createExecutionRecord(DataSourceExecution dataSourceExecution) {
        Optional<DataSourceIdentity> dataSourceIdentity = dataSourceService.findById(dataSourceExecution.dataSourceId);
        if (!dataSourceIdentity.isPresent()) {
            throw new IllegalArgumentException("Datasource with id " + dataSourceExecution.dataSourceId + " doesn't exist");
        }
        Execution execution = executionRepository.findById(dataSourceExecution.executionId);
        if (execution == null) {
            throw new IllegalArgumentException("Execution with id " + dataSourceExecution.dataSourceId + " doesn't exist");
        }

        List<DataRecord> records = dataSourceService.findRecords(dataSourceExecution.dataSourceId, dataSourceExecution.predicates);
        records.stream().forEach(r -> dataSourceExecution.recordIds.add(r.id));
        dataSourceExecutionRepository.insert(dataSourceExecution);

        return dataSourceExecution;
    }

    public void delete(DataSourceExecution executionRecord) {
        dataSourceExecutionRepository.delete(executionRecord);
    }

    public Optional<DataSourceExecution> findById(String dataSourceExecutionId) {
        DataSourceExecution execution = dataSourceExecutionRepository.findById(dataSourceExecutionId);
        if (execution != null) {
            return Optional.of(execution);
        }
        return Optional.empty();
    }

    public List<DataSourceExecution> findByExecutionId(String executionId) {
        return dataSourceExecutionRepository.findByExecutionId(executionId);
    }
}
