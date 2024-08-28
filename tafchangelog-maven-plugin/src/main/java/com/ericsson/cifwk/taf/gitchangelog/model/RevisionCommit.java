package com.ericsson.cifwk.taf.gitchangelog.model;

import com.google.common.base.MoreObjects;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 07/01/2016
 */
public class RevisionCommit {

    private String message;

    public RevisionCommit(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RevisionCommit that = (RevisionCommit) o;

        if (message != null ? !message.equals(that.message) : that.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("message", message)
                .toString();
    }
}
