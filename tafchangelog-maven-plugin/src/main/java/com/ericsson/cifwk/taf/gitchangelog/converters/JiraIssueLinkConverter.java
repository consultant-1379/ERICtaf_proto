package com.ericsson.cifwk.taf.gitchangelog.converters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ericsson.cifwk.taf.gitchangelog.converters.MessageConverter;
import com.ericsson.cifwk.taf.gitchangelog.utils.Defaults;
import org.apache.maven.plugin.logging.Log;

public class JiraIssueLinkConverter implements MessageConverter {

    private Pattern pattern;
    private final Log log;


    public JiraIssueLinkConverter(Log log) {
        this.log = log;
        this.pattern = Pattern.compile(Defaults.JIRA_TAGS);
    }

    public String formatMessage(String original) {
        try {
            Matcher matcher = this.pattern.matcher(original);
            String result = matcher.replaceAll("<a href=\"" + Defaults.JiraurlPrefix + "$0\" target=\"_blank\">$0</a>");
            return result;
        } catch (Exception e) {
            // log, but don't let this small setback fail the build
            log.info("Unable to parse issue tracking URL in commit message: " + original, e);
        }
        return original;
    }
}
