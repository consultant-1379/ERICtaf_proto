package com.ericsson.cifwk.taf.gitchangelog.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 08/01/2016
 */
public class RevisionHistory {

    private final Map<RevisionTag, List<RevisionCommit>> revisionTags;

    public RevisionHistory(Map<RevisionTag, List<RevisionCommit>> revisionTags) {
        Preconditions.checkArgument(revisionTags != null);
        this.revisionTags = Maps.newLinkedHashMap(revisionTags);
    }

    public Set<RevisionTag> getAllReleaseTags() {
        return revisionTags.keySet();
    }

    public List<RevisionCommit> getReleaseCommits(RevisionTag releaseTag) {
        return revisionTags.get(releaseTag);
    }
}
