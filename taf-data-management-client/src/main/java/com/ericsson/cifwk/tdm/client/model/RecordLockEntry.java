package com.ericsson.cifwk.tdm.client.model;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 18/02/2016
 */
public class RecordLockEntry {

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
