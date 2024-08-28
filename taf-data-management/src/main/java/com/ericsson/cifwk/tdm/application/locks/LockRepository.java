package com.ericsson.cifwk.tdm.application.locks;

import com.ericsson.cifwk.tdm.api.Lock;
import com.ericsson.cifwk.tdm.api.RecordLockEntry;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.jongo.Oid.withOid;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 17/02/2016
 */
@Repository
public class LockRepository {

    public static final String LOCKS_COLLECTION = "locks";
    public static final String DATA_RECORD_LOCKS_COLLECTION = "dataRecordLocks";

    @Autowired
    Jongo jongo;

    public void insert(Lock lock) {
        lock.createTime = new Date();
        jongo.getCollection(LOCKS_COLLECTION).insert(lock);
    }

    public void lockRecords(List<String> recordIds, Lock lock) {
        Object[] lockRecords = recordIds.stream()
                .map(recordId -> new RecordLockEntry(recordId, lock.id, lock.timeout))
                .collect(Collectors.toList())
                .toArray();
        jongo.getCollection(DATA_RECORD_LOCKS_COLLECTION).insert(lockRecords);
        update(lock);
    }

    public void delete(Lock lock) {
        jongo.getCollection(LOCKS_COLLECTION).remove(new ObjectId(lock.id));
        releaseLock(lock);
    }

    public void update(Lock lock) {
        jongo.getCollection(LOCKS_COLLECTION).save(lock);
    }


    public Lock findById(String lockId) {
        return jongo.getCollection(LOCKS_COLLECTION).findOne(withOid(lockId)).as(Lock.class);
    }

    public void releaseLock(Lock lock) {
        jongo.getCollection(DATA_RECORD_LOCKS_COLLECTION).remove("{lockId:#}", lock.id);
    }
}
