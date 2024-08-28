package com.ericsson.cifwk.docs;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.ericsson.cifwk.docs.Locations.fileToPath;
import static com.ericsson.cifwk.docs.Locations.getSourceLocation;
import static com.ericsson.cifwk.docs.Locations.replaceExtension;
import static com.google.common.io.Files.getNameWithoutExtension;

public class TocGenerator {

    private static final Logger log = LoggerFactory.getLogger(TocGenerator.class);

    private static final String TOC_TITLE_FILE = "_title.adoc";

    private final Path root;
    private final Document tree;
    private final Map<Path, TocEntry> entries;

    private TocGenerator(Path root, Document tree) {
        this.root = root;
        this.tree = tree;
        this.entries = new HashMap<>();
    }

    public static TocGenerator create(File treeFile)
            throws ParserConfigurationException, IOException, SAXException {
        Path root = fileToPath(treeFile).getParent();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document tree = builder.parse(treeFile);
        return new TocGenerator(root, tree);
    }

    public void register(Path srcPath, File srcFile, String srcFileName, String dstFileName, int level) {
        entries.put(srcPath, new TocEntry(srcFile, srcFileName, dstFileName, level));
    }

    public void generate(File out) throws IOException {
        StringBuilder tocBuilder = new StringBuilder()
                .append(partial(TOC_TITLE_FILE))
                .append("\n");

        Element rootElement = tree.getDocumentElement();
        generateToc(tocBuilder, rootElement);

        log.info("Writing TOC");
        String toc = tocBuilder.toString().replace("\r", "");
        Files.write(toc, out, Charsets.UTF_8);
    }

    private String partial(String relative) throws IOException {
        File partialFile = new File(root.toFile(), relative);
        return Files.toString(partialFile, Charsets.UTF_8);
    }

    private void generateToc(StringBuilder builder, Node node)
            throws IOException {
        generateToc(builder, "", node);
    }

    private void generateToc(StringBuilder builder, String parentPrefix, Node node)
            throws IOException {
        String prefix = node.getNodeName();
        if (!parentPrefix.isEmpty()) {
            prefix = parentPrefix + "/" + prefix;
        }
        Path path = getSourceLocation(prefix);
        TocEntry entry = entries.get(path);
        if (entry != null) {
            advanceToc(builder, entry);
        }
        NodeList childNodes = node.getChildNodes();
        int childNodeCount = childNodes.getLength();
        for (int i = 0; i < childNodeCount; i++) {
            Node childNode = childNodes.item(i);
            if (childNode instanceof Element) {
                generateToc(builder, prefix, childNode);
            }
        }
    }

    private void advanceToc(StringBuilder builder, TocEntry entry)
            throws IOException {
        String indent = Strings.repeat("*", entry.getLevel());
        String heading;
        try {
            heading = headingFromFile(entry.getSrcFile());
        } catch (IOException e) {
            heading = headingFromFileName(entry.getSrcFileName());
        }
        String linkPath = entry.getDstFileName();
        String link = replaceExtension(linkPath, "html");
        builder
                .append(indent)
                .append(" link:")
                .append(link)
                .append("[")
                .append(heading)
                .append("]\n");
    }

    private static String headingFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.replaceFirst("(?i)^[^a-z]*", "").trim();
            if (!line.isEmpty()) {
                break;
            }
        }
        br.close();
        return Strings.nullToEmpty(line);
    }

    private static String headingFromFileName(String fileName) {
        String fileTitle = getNameWithoutExtension(fileName).replace('-', ' ');
        return WordUtils.capitalize(fileTitle);
    }
}
