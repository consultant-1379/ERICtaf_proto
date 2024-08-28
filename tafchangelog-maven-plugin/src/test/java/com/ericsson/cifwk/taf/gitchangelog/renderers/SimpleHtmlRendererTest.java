package com.ericsson.cifwk.taf.gitchangelog.renderers;

import com.ericsson.cifwk.taf.gitchangelog.converters.JiraIssueLinkConverter;
import com.ericsson.cifwk.taf.gitchangelog.converters.TAFTagConverter;
import com.ericsson.cifwk.taf.gitchangelog.model.RevisionCommit;
import com.google.common.io.Files;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class SimpleHtmlRendererTest {

    private SimpleHtmlRenderer unit;

    @Before
    public void setUp() {
        Log log = mock(Log.class);
        unit = new SimpleHtmlRenderer(log, Files.createTempDir(), "changelog.html", false,
                new JiraIssueLinkConverter(log), new TAFTagConverter(log), "SimpleHtmlTemplate", new Properties(), "*");
    }

    @Test
    public void shouldGetAtomicCommits() throws Exception {
        List<RevisionCommit> atomicCommits = unit.getAtomicCommits("Simple commit");
        assertThat(atomicCommits, hasSize(1));
        assertThat(atomicCommits.get(0).getMessage(), equalTo("Simple commit"));

        atomicCommits = unit.getAtomicCommits("* Feature 1 * Feature 2 * Feature 3");
        assertThat(atomicCommits, hasSize(3));
        assertThat(atomicCommits.get(0).getMessage(), equalTo("Feature 1"));
        assertThat(atomicCommits.get(1).getMessage(), equalTo("Feature 2"));
        assertThat(atomicCommits.get(2).getMessage(), equalTo("Feature 3"));

    }
}