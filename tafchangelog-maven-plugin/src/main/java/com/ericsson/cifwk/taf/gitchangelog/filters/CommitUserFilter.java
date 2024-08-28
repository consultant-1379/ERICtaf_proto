package com.ericsson.cifwk.taf.gitchangelog.filters;

import com.ericsson.cifwk.taf.gitchangelog.utils.Defaults;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Arrays;
import java.util.List;

/**
 * Removes messages added by the specific User.
 */
public class CommitUserFilter implements CommitFilter {

    private static final List<String> aUSERS;

    static {
        aUSERS = Arrays.asList(Defaults.USERS.split(","));
    }

    public boolean renderCommit(RevCommit commit) {
        boolean isUserPresent = aUSERS.contains(commit.getCommitterIdent().getName());
        return !isUserPresent;
    }
}
