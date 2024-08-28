package com.ericsson.cifwk.tdm.presentation.controllers;

import com.ericsson.cifwk.tdm.api.DataRecord;
import com.ericsson.cifwk.tdm.api.DataRecords;
import com.ericsson.cifwk.tdm.api.DataSource;
import com.ericsson.cifwk.tdm.api.DataSourceIdentity;
import com.ericsson.cifwk.tdm.api.Execution;
import com.ericsson.cifwk.tdm.api.FilterCriteria;
import com.ericsson.cifwk.tdm.api.IntermediateDataSource;
import com.ericsson.cifwk.tdm.api.Pageable;
import com.ericsson.cifwk.tdm.application.executions.ExecutionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 10/02/2016
 */

@RestController
@RequestMapping("/api/executions")
public class ExecutionController {

    @Autowired
    ExecutionDataService executionDataService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Execution> getExecutions() {
        return executionDataService.findAll();
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Execution startExecution(@Valid @RequestBody Execution execution) {
        return executionDataService.createNew(execution);
    }

    @RequestMapping(value = "/{executionId}", method = RequestMethod.PATCH)
    public ResponseEntity<Execution> finishExecution(@PathVariable("executionId") String executionId) {
        Optional<Execution> execution = executionDataService.finishExecution(executionId);
        if (execution.isPresent()) {
            return new ResponseEntity<>(execution.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{executionId}", method = RequestMethod.GET)
    public ResponseEntity<Execution> getExecutionById(@PathVariable("executionId") String executionId) {
        Optional<Execution> execution = executionDataService.findById(executionId);
        if (execution.isPresent()) {
            return new ResponseEntity<>(execution.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{executionId}/datasources", method = RequestMethod.GET)
    public List<DataSourceIdentity> getDataSourcesForExecution(@PathVariable("executionId") Long executionId) {
        return null;
    }

    @RequestMapping(value = "/{executionId}/datasources", method = RequestMethod.POST)
    public DataSourceIdentity createDataSource(
            @PathVariable("executionId") Long executionId,
            @RequestBody DataSource dataSource) {
        return null;
    }

    @RequestMapping(value = "/{executionId}/datasources/{dataSourceId}/records", method = RequestMethod.PATCH)
    public IntermediateDataSource appendRecordsToDataSource(
            @PathVariable("executionId") Long executionId,
            @PathVariable("dataSourceId") Long dataSourceId,
            @RequestBody List<DataRecord> dataRecords) {
        return null;
    }

    @RequestMapping(value = "/{executionId}/datasources/{dataSourceId}", method = RequestMethod.DELETE)
    public IntermediateDataSource deleteDataSource(
            @PathVariable("executionId") Long executionId,
            @PathVariable("dataSourceId") Long dataSourceId) {
        return null;
    }

    @RequestMapping(value = "/{executionId}/datasources/{dataSourceId}/records", method = RequestMethod.GET)
    public DataRecords getRecords(
            @PathVariable("executionId") Long executionId,
            @PathVariable("dataSourceId") Long dataSourceId,
            FilterCriteria filterCriteria,
            Pageable pageable) {
        return null;
    }
}
