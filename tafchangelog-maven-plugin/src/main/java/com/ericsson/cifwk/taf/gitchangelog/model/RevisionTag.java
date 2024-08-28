package com.ericsson.cifwk.taf.gitchangelog.model;

import com.google.common.base.MoreObjects;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 07/01/2016
 */
public class RevisionTag {

    private String htmlContent;
    private String commitDate;
    private String versionType;

    public RevisionTag(String htmlContent, String commitDate, String versionType) {
        this.htmlContent = htmlContent;
        this.commitDate = commitDate;
        this.versionType = versionType;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RevisionTag that = (RevisionTag) o;

        if (commitDate != null ? !commitDate.equals(that.commitDate) : that.commitDate != null) return false;
        if (htmlContent != null ? !htmlContent.equals(that.htmlContent) : that.htmlContent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = htmlContent != null ? htmlContent.hashCode() : 0;
        result = 31 * result + (commitDate != null ? commitDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("htmlContent", htmlContent)
                .add("commitDate", commitDate)
                .toString();
    }
}
