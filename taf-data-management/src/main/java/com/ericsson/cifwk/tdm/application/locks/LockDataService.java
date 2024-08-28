package com.ericsson.cifwk.tdm.application.locks;

import com.ericsson.cifwk.tdm.api.DataSourceExecution;
import com.ericsson.cifwk.tdm.api.Lock;
import com.ericsson.cifwk.tdm.application.datasources.DataSourceRepository;
import com.ericsson.cifwk.tdm.application.datasources.DataSourceService;
import com.ericsson.cifwk.tdm.application.executions.DataSourceExecutionService;
import com.ericsson.cifwk.tdm.application.executions.ExecutionRepository;
import com.ericsson.cifwk.tdm.configuration.DbConfiguration;
import com.ericsson.cifwk.tdm.presentation.controllers.exceptions.LockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 18/02/2016
 */
@Service
public class LockDataService {

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    DataSourceExecutionService dataSourceExecutionService;

    @Autowired
    ExecutionRepository executionRepository;

    @Autowired
    DataSourceRepository dataSourceRepository;

    @Autowired
    LockRepository lockRepository;

    @Autowired
    DbConfiguration dbConfiguration;

    public Lock createLock(Lock lock) {
        DataSourceExecution executionRecord = null;
        try {
            executionRecord = dataSourceExecutionService.createExecutionRecord(lock.dataSourceExecution);
            lockRepository.insert(lock);
            lockRepository.lockRecords(executionRecord.recordIds, lock);
        } catch (Exception e) {
            if (executionRecord != null) {
                dataSourceExecutionService.delete(executionRecord);
            }
            lockRepository.delete(lock);
            throw new LockException("Failed to create lock");
        }
        return lock;
    }

    public void releaseLock(String lockId) {
        Lock lock = lockRepository.findById(lockId);
        if (lock == null) {
            throw new IllegalArgumentException("Lock with id " + lockId + " doesn't exist.");
        }
        lockRepository.releaseLock(lock);
    }
}
