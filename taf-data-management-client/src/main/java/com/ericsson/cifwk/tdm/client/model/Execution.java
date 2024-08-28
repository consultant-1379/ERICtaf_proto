package com.ericsson.cifwk.tdm.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 11/02/2016
 */
public class Execution {

    public String id;

    public String team;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyy HH:mm")
    public Date startTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyy HH:mm")
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
