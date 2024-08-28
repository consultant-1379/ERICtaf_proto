package com.ericsson.cifwk.taf.gitchangelog.renderers;

import com.ericsson.cifwk.taf.gitchangelog.converters.MessageConverter;
import com.ericsson.cifwk.taf.gitchangelog.model.RevisionCommit;
import com.ericsson.cifwk.taf.gitchangelog.model.RevisionHistory;
import com.ericsson.cifwk.taf.gitchangelog.model.RevisionTag;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTag;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SimpleHtmlRenderer extends FileRenderer {

    private String title;
    private final String htmlTemplateLocation;
    protected final MessageConverter messageConverter;
    protected final MessageConverter tagConverter;
    private final boolean fullGitMessage;
    private final Properties templateProperties;
    private final String multiLineCommitMsgSeparator;
    private final Map<RevisionTag, List<RevisionCommit>> revisionTags = Maps.newLinkedHashMap();
    private final List<RevisionCommit> lastCommits = Lists.newArrayList();
    private RevisionTag currentReleaseTag = null;

    public SimpleHtmlRenderer(Log log, File targetFolder, String filename, boolean fullGitMessage,
                              MessageConverter messageConverter, MessageConverter tagConverter,
                              String htmlTemplateName, Properties templateProperties, String multiLineCommitMsgSeparator) {
        super(log, targetFolder, filename);
        this.messageConverter = messageConverter;
        this.tagConverter= tagConverter;
        this.fullGitMessage = fullGitMessage;
        this.templateProperties = templateProperties;
        this.multiLineCommitMsgSeparator = multiLineCommitMsgSeparator;
        this.htmlTemplateLocation = String.format("/html/templates/%s.html.ftl", htmlTemplateName);
    }

    protected static String htmlEncode(String input) {
        input = StringEscapeUtils.escapeHtml4(input);
        return input.replaceAll("\\n", "<br/>");
    }


    public void renderHeader(String reportTitle) throws IOException {
        this.title = reportTitle;
    }


    public void renderTag(RevTag releaseTag, RevCommit commit) throws IOException {
        Preconditions.checkArgument(releaseTag != null);

        String convertedTag = tagConverter.formatMessage(htmlEncode(releaseTag.getTagName()));
        log.debug("unconverted tag = " + releaseTag.getTagName());
        log.debug("converted tag = " + convertedTag);
        if(convertedTag !=null ) {
            String tagDate = new SimpleDateFormat("d-MMM-yyyy").format(new Date(commit.getCommitTime() * 1000L));
            String versionType = "BUILD";
            String[] currentVersions = convertedTag.split("\\.");
            int currentMajor = Integer.parseInt(currentVersions[0]);
            int currentMinor = Integer.parseInt(currentVersions[1]);

            if (currentReleaseTag != null) {
                String[] prevVersions = currentReleaseTag.getHtmlContent().split("\\.");
                int prevMajor = Integer.parseInt(prevVersions[0]);
                int prevMinor = Integer.parseInt(prevVersions[1]);
                if (prevMajor != currentMajor)
                    currentReleaseTag.setVersionType("MAJOR");
                else if (prevMinor != currentMinor)
                    currentReleaseTag.setVersionType("MINOR");
            }

            RevisionTag revisionTag = new RevisionTag(convertedTag, tagDate, versionType);

            revisionTags.put(revisionTag, Lists.<RevisionCommit>newArrayList());
            this.currentReleaseTag = revisionTag;
        }
    }

    public void renderCommit(RevCommit commit) throws IOException {
        String message = (fullGitMessage) ? commit.getFullMessage() : commit.getShortMessage();
        message = StringUtils.trim(message);

        List<RevisionCommit> atomicCommits = getAtomicCommits(message);
        for (RevisionCommit atomicCommit : atomicCommits) {
            if (currentReleaseTag != null) {
                List<RevisionCommit> revisionCommits = revisionTags.get(currentReleaseTag);
                revisionCommits.add(atomicCommit);
            } else {
                lastCommits.add(atomicCommit);
            }
        }
    }

    @VisibleForTesting
    List<RevisionCommit> getAtomicCommits(String message) {
        List<RevisionCommit> atomicCommits = Lists.newArrayList();
        if (StringUtils.isNotBlank(multiLineCommitMsgSeparator)) {
            message = StringUtils.removeStartIgnoreCase(message, multiLineCommitMsgSeparator);
            String[] split = StringUtils.splitByWholeSeparator(message, multiLineCommitMsgSeparator);
            for (String atomicCommitStr : split) {
                String commitText = StringUtils.trim(atomicCommitStr);
                RevisionCommit revisionCommit = new RevisionCommit(messageConverter.formatMessage(htmlEncode(commitText)));
                atomicCommits.add(revisionCommit);
            }
        } else {
            RevisionCommit revisionCommit = new RevisionCommit(messageConverter.formatMessage(htmlEncode(message)));
            atomicCommits.add(revisionCommit);
        }
        return atomicCommits;
    }

    public void renderFooter() throws IOException {
        currentReleaseTag.setVersionType("MAJOR");
        templateProperties.put("revisionHistory", new RevisionHistory(revisionTags));
        templateProperties.put("lastCommits", lastCommits);
        String html = preProcessAsTemplate(htmlTemplateLocation, templateProperties);
        writer.append(html);
    }

}
