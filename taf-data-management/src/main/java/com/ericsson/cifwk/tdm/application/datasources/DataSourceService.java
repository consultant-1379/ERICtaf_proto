package com.ericsson.cifwk.tdm.application.datasources;

import com.ericsson.cifwk.tdm.api.DataRecord;
import com.ericsson.cifwk.tdm.api.DataSource;
import com.ericsson.cifwk.tdm.api.DataSourceIdentity;
import com.ericsson.cifwk.tdm.api.FilterCriteria;
import com.ericsson.cifwk.tdm.api.Lock;
import com.ericsson.cifwk.tdm.api.Pageable;
import com.ericsson.cifwk.tdm.api.RecordPredicate;
import com.ericsson.cifwk.tdm.application.locks.LockRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 17/02/2016
 */
@Service
public class DataSourceService {

    @Autowired
    DataSourceRepository dataSourceRepository;

    @Autowired
    LockRepository lockRepository;

    @Autowired
    FilterToPredicateConverter filterToPredicateConverter;

    public DataSourceIdentity create(DataSource dataSource) {
        persistIdentity(dataSource);
        persistRecordsWithIdentityReference(dataSource);
        return dataSource.identity;
    }

    public Optional<DataSourceIdentity> delete(String dataSourceId) {
        Optional<DataSourceIdentity> maybeIdentity = findById(dataSourceId);
        if (maybeIdentity.isPresent()) {
            DataSourceIdentity identity = maybeIdentity.get();
            identity.deleted = true;
            dataSourceRepository.update(identity);
            return Optional.of(identity);
        }
        return Optional.empty();
    }

    private void persistRecordsWithIdentityReference(DataSource dataSource) {
        dataSource.records.forEach(r -> r.dataSourceId = dataSource.identity.id);
        dataSourceRepository.insert(dataSource.records);
    }

    private void persistIdentity(DataSource dataSource) {
        dataSource.identity.createTime = new Date();
        dataSource.identity.createdBy = "enikoal";

        dataSourceRepository.insert(dataSource.identity);
    }

    public List<DataSourceIdentity> findAll() {
        return dataSourceRepository.findAll();
    }

    public Optional<DataSourceIdentity> findById(String identityId) {
        DataSourceIdentity byId = dataSourceRepository.findById(identityId);
        if (byId != null) {
            return Optional.of(byId);
        }
        return Optional.empty();
    }

    //TODO: take paging into account
    public List<DataRecord> findRecords(String dataSourceId, FilterCriteria filterCriteria, Pageable pageable) {
        return findRecords(dataSourceId, filterCriteria.predicates);
    }

    public List<DataRecord> findRecords(String dataSourceId, List<String> predicates) {
        List<RecordPredicate> recordPredicates = filterToPredicateConverter.convert(predicates);
        return dataSourceRepository.findRecords(dataSourceId, recordPredicates);
    }

    public List<DataRecord> getRecordsForLock(String lockId, Pageable pageable) {
        Lock lock = lockRepository.findById(lockId);
        if (lock == null) {
            return Lists.newArrayList();
        }
        return findRecords(lock.dataSourceExecution.dataSourceId, lock.dataSourceExecution.predicates);
    }
}
