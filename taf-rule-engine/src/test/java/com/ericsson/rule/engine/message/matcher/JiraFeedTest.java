package com.ericsson.rule.engine.message.matcher;

import net.rcarz.jiraclient.Status;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class JiraFeedTest {

    private JiraFeed jiraFeed;

    @Before
    public void setUp() {
        jiraFeed = new JiraFeed();
    }

    @Test
    public void shouldTest() throws Exception {
        Map<String,Status> stories = jiraFeed.mapOfStories();
        System.out.println(stories);
    }

}
