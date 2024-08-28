package com.ericsson.cifwk.taf.mm;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunTree {

    private static final Logger log = LoggerFactory.getLogger(RunTree.class);
    protected static final String WHITE = "\"#ffffff\"";
    protected static final String RED = "\"#d65252\"";
    protected static final String GREEN = "\"#76cd5a\"";
    protected static final String YELLOW = "\"#e5c249\"";

    public static final String FILE_NAME = "run.mm";
    protected static final String FOLDED = "FOLDED=\"true\"";
    protected static final String NODE_CLOSE = "</node>\n";
    public static final String REPORT_DIR = "target/report";
    protected String mainNodeColor = WHITE;
    private File file;
    protected String allSuites = "";
    protected final String runName;

    private String directory = null;

    private File getFile() {
        if (file == null) {
            if (directory != null) {
                new File(directory).mkdirs();
                file = new File(directory + "/" + FILE_NAME);
            } else
                file = new File(FILE_NAME);
        }
        return file;
    }

    enum Status {
        PASSED, FAILED, SKIPPED
    }

    private void setParentDir(String dir) {
        directory = dir;
    }

    private String getMainNode(boolean includeLink) {
        String link = null;
        if (includeLink) {
            link = runName + "/index.html";
        }
        return makeNode(runName, mainNodeColor, false, link);
    }

    protected String makeNode(String content, String color, boolean close,
            String link) {
        StringBuilder line = new StringBuilder("<node ");
        line.append("BACKGROUND_COLOR=" + color + " ");
        if (link != null)
            line.append("LINK=\"" + link + "\" ");
        if (content != null) {
            content = content.replaceAll("\"", "'");
            content = content.replaceAll("<", "&lt;");
            content = content.replaceAll(">", "&gt;");
            line.append("TEXT=\"" + content + "\" ");
        }
        line.append("POSITION=\"right\" ");
        if (!content.endsWith("run"))
            line.append(FOLDED);
        if (close)
            line.append("/");
        line.append(">\n");
        return line.toString();
    }

    protected String makeNode(String content, String color, boolean close) {
        return makeNode(content, color, close, null);
    }

    private void write(String content) {
        try {
            FileUtils.fileAppend(getFile().getAbsolutePath(), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(final String content, final boolean newFile) {
        try {
            if (newFile) {
                FileUtils.fileWrite(getFile().getAbsolutePath(), content);
            } else
                write(content);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void closeNode() {
        write(NODE_CLOSE);
    }

    public RunTree(String runName) {
        this.runName = runName;
    }

    protected String getColor(Status status) {
        String result = WHITE;
        switch (status) {
        case PASSED:
            result = GREEN;
            break;
        case FAILED:
            result = RED;
            break;
        case SKIPPED:
            result = YELLOW;
            break;
        default:
            result = YELLOW;
            break;
        }
        return result;
    }

    public void close() {
        close(false);
    }

    protected void close(boolean withMap) {
        if (withMap)
            write("<map version=\"0.9.0\">\n", true);
        write(getMainNode(!withMap), !withMap);
        write(allSuites);
        closeNode();
        if (withMap)
            write("</map>");
    }

    private static void putFile(String fileName) {
        URL file = RunTree.class.getResource("/webparts/" + fileName);
        try {
            if (file != null)
                FileUtils.copyURLToFile(file, new File(REPORT_DIR + "/"
                        + fileName));
            else
                System.err.println("Cannot copy " + fileName);
        } catch (IOException e) {
            log.error("File {} cannot be deployed due to error: {}", fileName,
                    e);
        }
    }

    private static void putHtmlParts() {
        putFile("index.html");
        putFile("flashobject.js");
        putFile("visorFreemind.swf");
    }

    private static boolean evaluate(String content) {
        return !(content.contains(RED) || content.contains(YELLOW));
    }

    public static boolean evaluate(File runFile) {
        try {
            return evaluate(FileUtils.fileRead(runFile));
        } catch (IOException e) {
        	log.error("IOException while reading: "+runFile.getAbsolutePath());
            return false;
        }
    }

    public static boolean combine(String mainNodeName,
            String directoryWithLogs, File... subFiles) {
        RunTree fullTree = new RunTree(mainNodeName);
        for (File subFile : subFiles) {
            try {
                fullTree.allSuites += FileUtils.fileRead(subFile);
                if (directoryWithLogs != null)
                    FileUtils.copyDirectoryStructure(
                            new File(subFile.getParent() + "/"
                                    + directoryWithLogs), new File(REPORT_DIR
                                            + "/" + subFile.getParentFile().getName()));
            } catch (IOException e) {
                log.error("Cannot process file {} due to exception {}",
                        subFile, e);
            }
        }
        boolean passed = evaluate(fullTree.allSuites);
        if (!passed)
            fullTree.mainNodeColor = RED;
        fullTree.setParentDir(REPORT_DIR);
        fullTree.close(true);
        putHtmlParts();
        return passed;
    }
}
