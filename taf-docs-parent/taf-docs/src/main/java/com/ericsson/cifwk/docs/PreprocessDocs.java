package com.ericsson.cifwk.docs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static com.ericsson.cifwk.docs.Locations.getExampleLocation;
import static com.ericsson.cifwk.docs.Locations.getSourceLocation;
import static com.ericsson.cifwk.docs.Locations.getTargetLocation;
import static com.google.common.io.Files.*;
import static java.nio.file.Files.newDirectoryStream;
import static java.nio.file.Files.walkFileTree;

public final class PreprocessDocs {

    private static final Logger log = LoggerFactory.getLogger(PreprocessDocs.class);

    private static final String INDEX_FILE = "index.adoc";
    private static final String TOC_FILE = "_toc.xml";

    public static void main(String[] args) throws Exception {
        Path src = getSourceLocation("asciidoc");
        Path dst = getTargetLocation("preprocessed-docs");
        TocGenerator toc = TocGenerator.create(new File(src.toFile(), TOC_FILE));
        DocsPreprocessor preprocessor = DocsPreprocessor.create(src, dst, toc);
        walkFileTree(src, preprocessor);
        File tocFile = new File(dst.toFile(), INDEX_FILE);
        toc.generate(tocFile);

        Path examplesSrc = getExampleLocation(".");
        Path examplesDst = dst.resolve("examples");
        RecursiveCopier copier = new RecursiveCopier(examplesSrc, examplesDst);
        walkFileTree(examplesSrc, copier);
    }

    private static class DocsPreprocessor implements FileVisitor<Path> {

        private final Path src;
        private final Path dst;
        private final TocGenerator toc;

        private int level = 0;

        private DocsPreprocessor(Path src, Path dst, TocGenerator toc) {
            this.src = src;
            this.dst = dst;
            this.toc = toc;
        }

        public static DocsPreprocessor create(Path src, Path dst, TocGenerator toc)
                throws ParserConfigurationException, IOException, SAXException {
            return new DocsPreprocessor(src, dst, toc);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs)
                throws IOException {
            File dir = path.toFile();
            String dirName = dir.getName();
            if (dirName.startsWith("_")) {
                log.debug("Skipping file: {}", dir);
                return FileVisitResult.SKIP_SUBTREE;
            } else {
                if ((level > 0) && directoryContainsExt(path, "adoc")) {
                    Path srcPath = path.resolve(INDEX_FILE);
                    File srcFile = srcPath.toFile();
                    String dstFileName = compatibleTargetFile(srcPath).getName();
                    toc.register(path, srcFile, dirName, dstFileName, level);
                }
                level++;
                return FileVisitResult.CONTINUE;
            }
        }

        private boolean directoryContainsExt(Path path, String ext)
                throws IOException {
            DirectoryStream<Path> childPaths = newDirectoryStream(path);
            for (Path childPath : childPaths) {
                File file = childPath.toFile();
                if (file.isFile() && getFileExtension(file.getPath()).equals(ext)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
                throws IOException {
            File file = path.toFile();
            String fileName = file.getName();
            if (!fileName.startsWith("_")) {
                log.info("Including file: {}", file);
                File dstFile = compatibleTargetFile(path);
                createParentDirs(dstFile);
                copy(file, dstFile);
                if (getFileExtension(fileName).equals("adoc") && !fileName.equals(INDEX_FILE)) {
                    toc.register(path, path.toFile(), fileName, dstFile.getName(), level);
                }
            } else {
                log.debug("Skipping file: {}", file);
            }
            return FileVisitResult.CONTINUE;
        }

        private File compatibleTargetFile(Path path) {
            Path relative = src.relativize(path);
            String name = relative.toString();
            if (getFileExtension(name).equals("adoc")) {
                name = name
                        .replaceAll("[\\\\/]", "-")
                        .replaceFirst("-index\\.adoc$", ".adoc");
            }
            return dst.resolve(name).toFile();
        }

        @Override
        public FileVisitResult visitFileFailed(Path path, IOException exc)
                throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path path, IOException exc)
                throws IOException {
            level--;
            return FileVisitResult.CONTINUE;
        }

    }
}
