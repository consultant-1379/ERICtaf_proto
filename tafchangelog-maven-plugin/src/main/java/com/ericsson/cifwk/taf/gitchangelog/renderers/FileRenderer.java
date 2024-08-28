package com.ericsson.cifwk.taf.gitchangelog.renderers;

import com.google.common.base.Throwables;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

public abstract class FileRenderer implements ChangeLogRenderer {

    protected Writer writer;
    protected final Log log;

    public FileRenderer(Log log, File targetFolder, String filename) {
        this.log = log;
        File file = new File(targetFolder, filename);
        log.debug("Creating git changelog at " + file.getAbsolutePath());
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public void close() {
        if (writer != null) {
            try {
                writer.flush();
            } catch (IOException e) {
                log.error("Could not flush file to disk", e);
            }
            try {
                writer.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    protected String preProcessAsTemplate(String templateLocation, Properties properties) {
        try {
            Configuration cfg = new Configuration();
            cfg.setClassForTemplateLoading(this.getClass(), "/");
            cfg.setDefaultEncoding("UTF-8");
            Template template = cfg.getTemplate(templateLocation);
            StringWriter stringWriter = new StringWriter();
            template.process(properties, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
