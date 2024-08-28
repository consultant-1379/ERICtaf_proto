package com.ericsson.cifwk.taf.gitchangelog.renderers;

import java.io.IOException;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTag;

public interface ChangeLogRenderer {

	public void renderHeader(String reportTitle) throws IOException;

	public void renderTag(RevTag tag,RevCommit commit) throws IOException;

	public void renderCommit(RevCommit commit) throws IOException;

	public void renderFooter() throws IOException;

	public void close();

}
