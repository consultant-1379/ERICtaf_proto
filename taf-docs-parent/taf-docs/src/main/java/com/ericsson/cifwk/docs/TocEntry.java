package com.ericsson.cifwk.docs;

import java.io.File;

public class TocEntry {

    private final File srcFile;
    private final String srcFileName;
    private final String dstFileName;
    private final int level;

    public TocEntry(File srcFile, String srcFileName, String dstFileName, int level) {
        this.srcFile = srcFile;
        this.srcFileName = srcFileName;
        this.dstFileName = dstFileName;
        this.level = level;
    }

    public File getSrcFile() {
        return srcFile;
    }

    public String getSrcFileName() {
        return srcFileName;
    }

    public String getDstFileName() {
        return dstFileName;
    }

    public int getLevel() {
        return level;
    }
}
