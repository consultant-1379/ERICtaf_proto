package com.ericsson.cifwk.taf.gitchangelog.maven;

import com.ericsson.cifwk.taf.gitchangelog.maven.GenerateMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.sink.Sink;

import java.io.File;
import java.util.Locale;

/**
 * Goal which generates a changelog based on commits made to the current git repo.
 */
@Mojo(
        name = "report"
)
public class GenerateReport extends GenerateMojo implements MavenReport {


    public void generate(Sink sink, Locale locale)
            throws MavenReportException
    {
        try {
            execute();
        } catch (MojoExecutionException ex) {
            getLog().error(ex.getMessage(), ex);
        } catch (MojoFailureException ex) {
            getLog().error(ex.getMessage(), ex);
        }
    }


    public String getOutputName()
    {
        return "gitlog";
    }


    public String getCategoryName()
    {
        return CATEGORY_PROJECT_REPORTS;
    }


    public String getName(Locale locale)
    {
        return "GitLog";
    }


    public String getDescription(Locale locale)
    {
        return "Generate changelog from git SCM.";
    }

    /**
     * When running as a reporting plugin, the output directory is fixed, set by the reporting cycle.
     * @param file
     */
    public void setReportOutputDirectory(File file)
    {
        outputDirectory = file;
    }

    public File getReportOutputDirectory()
    {
        return outputDirectory;
    }


    public boolean isExternalReport()
    {
        return true;
    }

    public boolean canGenerateReport()
    {
        return true;
    }

}