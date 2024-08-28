package com.ericsson.cifwk.tdm.api;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 18/02/2016
 */
public class RecordLockEntry {

    @MongoId
    @MongoObjectId
    public String id;

    public Date expireAt;
    public String dataRecordId;
    public String lockId;

    public RecordLockEntry() {
    }

    public RecordLockEntry(String dataRecordId, String lockId, int timeoutSeconds) {
        this.dataRecordId = dataRecordId;
        this.lockId = lockId;

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, timeoutSeconds);
        this.expireAt = instance.getTime();
    }
}
