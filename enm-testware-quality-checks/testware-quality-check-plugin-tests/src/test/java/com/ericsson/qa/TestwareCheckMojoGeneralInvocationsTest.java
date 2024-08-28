package com.ericsson.qa;

import static com.ericsson.qa.MavenTestRunner.runPom;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 11/10/2016
 */
public class TestwareCheckMojoGeneralInvocationsTest {

    private StringBuilder outputBuffer = new StringBuilder();

    @Test
    public void defaultRun() throws Exception {
        // Checkstyle should fail because of problems in MyTest.java
        assertThat(runPom("default_pom.xml", outputBuffer)).isFalse();
        String output = outputBuffer.toString();
        verifyRulesUnpacked(output, true);
        verifyCheckstyleRan(output, true);
        verifyHuntBugsRan(output, true);
        assertThat(output).contains("Checkstyle violations");
    }

    @Test
//    @Ignore
    public void noCheckStyle() throws Exception {
        assertThat(runPom("no_checkstyle_pom.xml", outputBuffer)).isTrue();
        String output = outputBuffer.toString();
        verifyRulesUnpacked(output, true);
        verifyCheckstyleRan(output, false);
        verifyPmdRan(output, true);
        verifyFindbugsRan(output, true);
        verifyHuntBugsRan(output, true);
    }


    private void verifyRulesUnpacked(String output, boolean trueOrFalse) {
        verifySubPluginRan(output, "RULE UNPACKING", trueOrFalse);
    }

    private void verifyCheckstyleRan(String output, boolean trueOrFalse) {
        verifySubPluginRan(output, "CHECKSTYLE CHECKS", trueOrFalse);
    }

    private void verifyPmdRan(String output, boolean trueOrFalse) {
        verifySubPluginRan(output, "PMD .* CHECKS", trueOrFalse);
    }

    private void verifyFindbugsRan(String output, boolean trueOrFalse) {
        verifySubPluginRan(output, "FINDBUGS CHECKS .*", trueOrFalse);
    }

    private void verifyHuntBugsRan(String output, boolean trueOrFalse) {
        verifySubPluginRan(output, "HUNTBUGS CHECKS .*", trueOrFalse);
    }

    private void verifySubPluginRan(String output, String pluginFlowMessage, boolean trueOrFalse) {
        verifyContainsString(output, pluginFlowMessage + " STARTED", trueOrFalse);
        verifyContainsString(output, pluginFlowMessage + " FINISHED", trueOrFalse);
    }

    private void verifyContainsString(String stack, String needle, boolean trueOrFalse) {
        if (trueOrFalse) {
            assertThat(stack).containsMatch(needle);
            assertThat(stack).containsMatch(needle);
        } else {
            assertThat(stack).doesNotContainMatch(needle);
            assertThat(stack).doesNotContainMatch(needle);
        }
    }

}
