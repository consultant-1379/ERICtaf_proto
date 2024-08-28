package com.ericsson.cifwk.taf.gitchangelog.tests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * PatternMatcherTest
 * Used to test the all the Pattern matchings done in the gitchangelogplugin
 */

public class PatternMatcherTest {

    private static final Logger logger = LoggerFactory.getLogger(PatternMatcherTest.class);
    
    /**
     * Verify parsing the Tags
     */
    @Test
    public void shouldParseTags() {

        String str="ERICtaf_util-asdfasd2.6.301sdfgasdf3.6.12";
        Pattern pat = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

        Matcher mat = pat.matcher(str);

        if(mat.find())
            logger.info("Match: " + mat.group());        

    }
    
    /**
     * Verify parsing the Jira Tickets
     */
    @Test
    public void shouldParseJiraTickets()        
    {
        String str="DURACI-4066 Additional parameters added to .createarne command http://jira-nam.lmera.ericsson.se/browse/CIS-9563";
        String urlPrefix="http://jira-nam.lmera.ericsson.se/browse/";
        Pattern pat = Pattern.compile("(DURACI|CI[A-Z])-[0-9]+");  

        Matcher matcher = pat.matcher(str);        
        
        String result = matcher.replaceAll("<a href=\"" + urlPrefix + "$0\">$0</a>");
        logger.info(result);
    }

}
