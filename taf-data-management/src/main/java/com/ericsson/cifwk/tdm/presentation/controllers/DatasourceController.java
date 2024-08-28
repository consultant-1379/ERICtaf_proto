package com.ericsson.cifwk.tdm.presentation.controllers;

import com.ericsson.cifwk.tdm.api.DataRecord;
import com.ericsson.cifwk.tdm.api.DataRecords;
import com.ericsson.cifwk.tdm.api.DataSource;
import com.ericsson.cifwk.tdm.api.DataSourceIdentity;
import com.ericsson.cifwk.tdm.api.FilterCriteria;
import com.ericsson.cifwk.tdm.api.Lock;
import com.ericsson.cifwk.tdm.api.Pageable;
import com.ericsson.cifwk.tdm.application.datasources.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 10/02/2016
 */

@RestController
@RequestMapping("/api/datasources")
public class DataSourceController {

    @Autowired
    DataSourceService dataSourceService;

    @RequestMapping(method = RequestMethod.GET)
    public List<DataSourceIdentity> getDataSourceIdentities() {
        return dataSourceService.findAll();
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public DataSourceIdentity createDataSource(@RequestBody DataSource dataSource) {
        return dataSourceService.create(dataSource);
    }

    @RequestMapping(value = "/{dataSourceId}", method = RequestMethod.GET)
    public ResponseEntity<DataSourceIdentity> getDataSourceById(@PathVariable("dataSourceId") String dataSourceId) {
        Optional<DataSourceIdentity> identity = dataSourceService.findById(dataSourceId);
        if (identity.isPresent()) {
            return new ResponseEntity<>(identity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{dataSourceId}", method = RequestMethod.DELETE)
    public ResponseEntity<DataSourceIdentity> deleteDataSource(@PathVariable("dataSourceId") String dataSourceId) {
        Optional<DataSourceIdentity> identity = dataSourceService.delete(dataSourceId);
        if (identity.isPresent()) {
            return new ResponseEntity<>(identity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{dataSourceId}/versions", method = RequestMethod.GET)
    public List<Integer> getDataSourceVersions(@PathVariable("dataSourceId") Long dataSourceId) {
        return null;
    }

    @RequestMapping(value = "/{dataSourceId}/records", method = RequestMethod.GET)
    public List<DataRecord> getRecords(@PathVariable("dataSourceId") String dataSourceId,
                                       FilterCriteria filterCriteria,
                                       Pageable pageable) {
        return dataSourceService.findRecords(dataSourceId, filterCriteria, pageable);
    }

    @RequestMapping(value = "/{dataSourceId}/records", method = RequestMethod.POST)
    public DataSourceIdentity overrideRecordsToDataSource(@PathVariable("dataSourceId") Long dataSourceId,
                                                          @RequestBody DataRecords dataSource) {
        return null;
    }

    @RequestMapping(value = "/{dataSourceId}/records", method = RequestMethod.PATCH)
    public DataSourceIdentity appendRecordsToDataSource(@PathVariable("dataSourceId") Long dataSourceId,
                                                        @RequestBody DataRecord dataSource) {
        return null;
    }

    @RequestMapping(value = "/{dataSourceId}/records", method = RequestMethod.DELETE)
    public DataSourceIdentity deleteRecordsFromDataSource(@PathVariable("dataSourceId") Long dataSourceId) {
        return null;
    }

    @RequestMapping(value = "/{dataSourceId}/locks", method = RequestMethod.GET)
    public List<Lock> getLocks(@PathVariable("dataSourceId") Long dataSourceId) {
        return null;
    }


    @RequestMapping(value = "/{dataSourceId}/versions/{version}", method = RequestMethod.GET)
    public DataSourceIdentity getDataSourceByIdAndVersion(@PathVariable("dataSourceId") Long dataSourceId,
                                                          @PathVariable("version") Integer version) {
        return null;
    }

    @RequestMapping(value = "/{dataSourceId}/versions/{version}/records", method = RequestMethod.GET)
    public DataRecords getDataSourceByIdAndVersion(@PathVariable("dataSourceId") Long dataSourceId,
                                                   @PathVariable("version") Integer version,
                                                   FilterCriteria filterCriteria,
                                                   Pageable pageable) {
        return null;
    }

    @RequestMapping(value = "/{dataSourceId}/versions/{version}/locks", method = RequestMethod.GET)
    public DataRecords getLocksForVersion(@PathVariable("dataSourceId") Long dataSourceId,
                                          @PathVariable("version") Integer version) {
        return null;
    }
}
