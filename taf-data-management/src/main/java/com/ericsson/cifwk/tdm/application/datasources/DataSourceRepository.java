package com.ericsson.cifwk.tdm.application.datasources;

import com.ericsson.cifwk.tdm.api.DataRecord;
import com.ericsson.cifwk.tdm.api.DataSourceIdentity;
import com.ericsson.cifwk.tdm.api.RecordPredicate;
import org.jongo.Jongo;
import org.jongo.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

import static org.jongo.Oid.withOid;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 17/02/2016
 */
@Repository
public class DataSourceRepository {

    public static final String DATA_SOURCES_COLLECTION = "dataSources";
    public static final String DATA_RECORDS_COLLECTION = "dataRecords";

    @Autowired
    Jongo jongo;

    public void insert(DataSourceIdentity dataSourceIdentity) {
        jongo.getCollection(DATA_SOURCES_COLLECTION).insert(dataSourceIdentity);
    }

    public void insert(Collection<DataRecord> dataRecords) {
        jongo.getCollection(DATA_SOURCES_COLLECTION).insert(dataRecords.toArray());
    }

    public DataSourceIdentity update(DataSourceIdentity dataSourceIdentity) {
        jongo.getCollection(DATA_SOURCES_COLLECTION).save(dataSourceIdentity);
        return dataSourceIdentity;
    }

    public List<DataSourceIdentity> findAll() {
        MongoCursor<DataSourceIdentity> result = jongo.getCollection(DATA_SOURCES_COLLECTION).find().as(DataSourceIdentity.class);
        List<DataSourceIdentity> executions = new ArrayList<>(result.count());
        result.forEach(executions::add);
        return executions;
    }

    public DataSourceIdentity findById(String identityId) {
        return jongo.getCollection(DATA_SOURCES_COLLECTION).findOne(withOid(identityId)).as(DataSourceIdentity.class);
    }

    public DataSourceIdentity findByIdAndVersion(String identityId, int version) {
        String query = String.format("{_id: {$oid:\"%s\"}, version: %d}", identityId, version);
        return jongo.getCollection(DATA_SOURCES_COLLECTION).findOne(query).as(DataSourceIdentity.class);
    }

    public List<DataRecord> findRecords(String dataSourceId, List<RecordPredicate> recordPredicates) {
        String query = buildSearchQuery(dataSourceId, recordPredicates);
        MongoCursor<DataRecord> result = jongo.getCollection(DATA_RECORDS_COLLECTION).find(query).as(DataRecord.class);
        List<DataRecord> dataRecords = new ArrayList<>(result.count());
        result.forEach(dataRecords::add);
        return dataRecords;
    }

    private String buildSearchQuery(String dataSourceId, List<RecordPredicate> recordPredicates) {
        StringJoiner stringJoiner = new StringJoiner(",", "{", "}");
        stringJoiner.add("dataSourceId:'" + dataSourceId + "'");
        recordPredicates.forEach(t -> stringJoiner.add(t.toString()));
        return stringJoiner.toString();
    }
}
