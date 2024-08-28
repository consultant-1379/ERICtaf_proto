package com.ericsson.cifwk.tdm.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 11/02/2016
 */
public class Execution {

    @MongoId
    @MongoObjectId
    public String id;

    @NotNull
    public String team;

    /**
     * Include mapping to test suite, test case in properties
     */
    public Map<String, Object> properties;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy HH:mm")
    public Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy HH:mm")
    public Date endTime;

    public String getId() {
        return id;
    }

    public String getTeam() {
        return team;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void finish() {
        this.endTime = new Date();
    }

    public void start() {
        this.startTime = new Date();
    }
}
