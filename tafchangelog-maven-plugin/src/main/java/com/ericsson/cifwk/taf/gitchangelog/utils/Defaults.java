package com.ericsson.cifwk.taf.gitchangelog.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import com.ericsson.cifwk.taf.gitchangelog.filters.CommitFilter;
import com.ericsson.cifwk.taf.gitchangelog.filters.CommitUserFilter;
import com.ericsson.cifwk.taf.gitchangelog.filters.DuplicateCommitMessageFilter;
import com.ericsson.cifwk.taf.gitchangelog.filters.MavenReleasePluginMessageFilter;
import com.ericsson.cifwk.taf.gitchangelog.filters.MergeCommitFilter;

public class Defaults {
	public static final List<CommitFilter> DEFAULT_COMMIT_FILTERS = Arrays.asList(
			new MavenReleasePluginMessageFilter(),
			new CommitUserFilter(),
			new MergeCommitFilter(),
			new DuplicateCommitMessageFilter()
	);
	
	public static final List<CommitFilter> COMMIT_FILTERS;

	public static final String USERS="Jenkins Release,ENM_Jenkins,Linux CI Admin";

	public static final String JIRA_TAGS="(DURACI|CI[A-Z]|TOR[A-Z]|RTD|BNLC|NSS)-[0-9]+";

	public static final String JiraurlPrefix="http://jira-nam.lmera.ericsson.se/browse/";
	
	static {
		COMMIT_FILTERS = new ArrayList<CommitFilter>();
		COMMIT_FILTERS.addAll(DEFAULT_COMMIT_FILTERS);
		
		Iterator<CommitFilter> it = ServiceLoader.load(CommitFilter.class).iterator();
		while (it.hasNext()){
			COMMIT_FILTERS.add(it.next());
		}
	}
}
