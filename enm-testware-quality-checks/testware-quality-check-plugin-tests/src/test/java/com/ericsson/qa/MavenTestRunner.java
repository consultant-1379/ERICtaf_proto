package com.ericsson.qa;

import com.google.common.base.Throwables;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 11/10/2016
 */
class MavenTestRunner {

    private static final String POM_XML = "pom.xml";

    static boolean runPom(String pomName, final StringBuilder outputBuffer) throws Exception {
        File sampleSourceDirectory = new File("target/classes/sample-testware");

        // Copy to a separate directory for the results not to clash
        String randomFolder = RandomStringUtils.randomAlphanumeric(5);
        File workingDirectory = new File("target/" + randomFolder);
        FileUtils.copyDirectory(sampleSourceDirectory, workingDirectory);
        // Rename POM file to pom.xml to let child modules to refer to parent correctly

        File pomFile = new File(workingDirectory, POM_XML);
        if (!StringUtils.equals(pomName, POM_XML)) {
            File originalFile = new File(workingDirectory, pomName);
            FileUtils.copyFile(originalFile, pomFile);
        }

        Properties testProperties = new Properties();
        ClassLoader classLoader = MavenTestRunner.class.getClassLoader();
        testProperties.load(classLoader.getResourceAsStream("test.properties"));

        InvocationRequest request = new DefaultInvocationRequest();
        request.setGoals(goals("clean", "package"));
        request.setProperties(testProperties);
        request.setPomFile(pomFile);

        Invoker invoker = new DefaultInvoker();
        invoker.setWorkingDirectory(workingDirectory);
        invoker.setOutputHandler(new InvocationOutputHandler() {
            @Override
            public void consumeLine(String s) {
                System.out.println(s);
                outputBuffer.append(s);
            }
        });
        invoker.setErrorHandler(new InvocationOutputHandler() {
            @Override
            public void consumeLine(String s) {
                System.err.println(s);
                outputBuffer.append(s);
            }
        });

        try {
            InvocationResult result = invoker.execute(request);
            return result.getExitCode() == 0;
        } catch (MavenInvocationException e) {
            throw Throwables.propagate(e);
        }
    }

    private static List<String> goals(String ... goals){
        return Arrays.asList(goals);
    }

}
