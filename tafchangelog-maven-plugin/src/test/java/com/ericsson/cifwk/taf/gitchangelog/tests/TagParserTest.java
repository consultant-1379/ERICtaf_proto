package com.ericsson.cifwk.taf.gitchangelog.tests;

import com.ericsson.cifwk.taf.gitchangelog.converters.TAFTagConverter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.truth.Truth.assertThat;

/**
 * PatternMatcherTest
 * Used to test the all the Pattern matchings done in the gitchangelogplugin
 */

public class TagParserTest {

    private static final Logger logger = LoggerFactory.getLogger(TagParserTest.class);
    
    /**
     * Verify parsing the Tags
     */
    @Test
    public void shouldParseTags() {
        TAFTagConverter tafTagConverter = new TAFTagConverter(null);

        String str="ERICtaf_util-asdfasd2.6.301sdfgasdf3.6.12";
        String formattedStr = tafTagConverter.formatMessage(str);
        logger.info(formattedStr);
        assertThat(formattedStr.trim()).isEqualTo("2.6.301");

        String str2="ERICtaf_util-asdfasd2.6sdfgasdf3.6.12";
        formattedStr = tafTagConverter.formatMessage(str2);
        logger.info(formattedStr);
        assertThat(formattedStr.trim()).isEqualTo("3.6.12");

        String str3="ERICtaf_util-asdfasd2.6sdfgasdf3.6";
        formattedStr = tafTagConverter.formatMessage(str3);
        logger.info(formattedStr);
        assertThat(formattedStr.trim()).isEqualTo("2.6");
    }


}
