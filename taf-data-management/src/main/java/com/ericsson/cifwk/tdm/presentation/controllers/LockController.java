package com.ericsson.cifwk.tdm.presentation.controllers;

import com.ericsson.cifwk.tdm.api.DataRecord;
import com.ericsson.cifwk.tdm.api.DataRecords;
import com.ericsson.cifwk.tdm.api.Lock;
import com.ericsson.cifwk.tdm.api.Pageable;
import com.ericsson.cifwk.tdm.application.datasources.DataSourceService;
import com.ericsson.cifwk.tdm.application.locks.LockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO: include API versioning
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 10/02/2016
 */

@RestController
@RequestMapping("/api/locks")
public class LockController {

    @Autowired
    LockDataService lockDataService;

    @Autowired
    DataSourceService dataSourceService;

    @RequestMapping(method = RequestMethod.POST)
    public Lock createDataRecordLock(@RequestBody Lock lock) {
        return lockDataService.createLock(lock);
    }


    @RequestMapping(value = "/{lockId}/records", method = RequestMethod.GET)
    public List<DataRecord> getDataRecordsForLock(@PathVariable("lockId") String lockId,
                                                  Pageable pageable) {
        return dataSourceService.getRecordsForLock(lockId, pageable);
    }


    @RequestMapping(value = "/{lockId}", method = RequestMethod.DELETE)
    public void releaseLock(@PathVariable("lockId") String lockId) {
        lockDataService.releaseLock(lockId);
    }
}
