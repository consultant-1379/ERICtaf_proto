package hudson.plugins.logparser;

import hudson.FilePath;
import hudson.console.ConsoleNote;
import hudson.model.AbstractBuild;
import hudson.remoting.VirtualChannel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.ericsson.taf.hackathon.stackoverflow.impl.LogObject;
import com.ericsson.taf.hackathon.stackoverflow.impl.StackOverflowServiceImpl;
import com.ericsson.taf.hackathon.stackoverflow.intface.StackOverflowService;

public class LogParserParser {
	
	private Set<String> matchedRules = new HashSet<String>();
	
	final Logger logger = Logger.getLogger(getClass().getName());

    final private HashMap<String, Integer> statusCount = new HashMap<String, Integer>();

    final private HashMap<String, BufferedWriter> writers = new HashMap<String, BufferedWriter>();

    final private HashMap<String, String> linkFiles = new HashMap<String, String>();

    final private String[] parsingRulesArray;

    final private Pattern[] compiledPatterns;

    final private CompiledPatterns compiledPatternsPlusError;

    // if key is 3-ERROR it shows how many errors are in section 3
    final private HashMap<String, Integer> statusCountPerSection = new HashMap<String, Integer>();

    final private ArrayList<String> headerForSection = new ArrayList<String>();

    private int sectionCounter = 0;

    final private LogParserDisplayConsts displayConstants = new LogParserDisplayConsts();

    final private VirtualChannel channel;

    final private boolean preformattedHtml;

    public LogParserParser(final FilePath parsingRulesFile, final boolean preformattedHtml, final VirtualChannel channel) throws IOException {
        
        logger.info("Parsing file is: "+parsingRulesFile);

        // Count of lines in this status
        statusCount.put(LogParserConsts.ERROR, 0);
        statusCount.put(LogParserConsts.WARNING, 0);
        statusCount.put(LogParserConsts.INFO, 0);
        
        this.parsingRulesArray = loadRules();

//        this.parsingRulesArray = LogParserUtils.readParsingRules(parsingRulesFile);

        // This causes each regular expression to be compiled once for better
        // performance
        this.compiledPatternsPlusError = LogParserUtils.compilePatterns(this.parsingRulesArray, logger);
        this.compiledPatterns = this.compiledPatternsPlusError.getCompiledPatterns();

        this.preformattedHtml = preformattedHtml;
        this.channel = channel;
    }

    /**
	 * @return
	 */
	private String[] loadRules() {
		String[] rules = {"error /Too many open files/", "error /(?i)^error/"};
		return rules;
	}

	/*
     * This method creates the parsed log file : log.html It also creates the
     * lists of links to these errors/warnings/info messages respectively :
     * errorLinks.html, warningLinks.html, infoLinks.html
     */
    public LogParserResult parseLog(final AbstractBuild build) throws IOException, InterruptedException {

        // init logger
        final Logger logger = Logger.getLogger(getClass().getName());

        logger.info("Karthik: Starting Parsing");

        // Get console log file
        final File logFile = build.getLogFile();
        final String logDirectory = logFile.getParent();
        final String logFileLocation = logFile.getAbsolutePath();
        final FilePath filePath = new FilePath(new File(logFileLocation));

        // Determine parsed log files
        final String parsedFilePath = logDirectory + "/log_content.html";
        final String errorLinksFilePath = logDirectory + "/logerrorLinks.html";
        final String warningLinksFilePath = logDirectory + "/logwarningLinks.html";
        final String infoLinksFilePath = logDirectory + "/loginfoLinks.html";
        final String buildRefPath = logDirectory + "/log_ref.html";
        final String buildWrapperPath = logDirectory + "/log.html";

        // Record file paths in hash
        linkFiles.put(LogParserConsts.ERROR, errorLinksFilePath);
        linkFiles.put(LogParserConsts.WARNING, warningLinksFilePath);
        linkFiles.put(LogParserConsts.INFO, infoLinksFilePath);

        // Open console log for reading and all other files for writing
        final BufferedWriter writer = new BufferedWriter(new FileWriter(parsedFilePath));

        // Record writers to links files in hash
        writers.put(LogParserConsts.ERROR, new BufferedWriter(new FileWriter(errorLinksFilePath)));
        writers.put(LogParserConsts.WARNING, new BufferedWriter(new FileWriter(warningLinksFilePath)));
        writers.put(LogParserConsts.INFO, new BufferedWriter(new FileWriter(infoLinksFilePath)));

        // Loop on the console log as long as there are input lines and parse
        // line by line
        // At the end of this loop, we will have:
        // - a parsed log with colored lines
        // - 3 links files which will be consolidated into one referencing html
        // file.

        // Create dummy header and section for beginning of log
        final String shortLink = " <a target=\"content\" href=\"log_content.html\">Beginning of log</a>";
        LogParserWriter.writeHeaderTemplateToAllLinkFiles(writers, sectionCounter); // This enters a line which will later be
                                                                                    // replaced by the actual header and count for
                                                                                    // this header
        headerForSection.add(shortLink);
        writer.write(LogParserConsts.getHtmlOpeningTags());
        if (this.preformattedHtml)
            writer.write("<pre>");
        // Read bulks of lines, parse
        final int linesInLog = LogParserUtils.countLines(logFileLocation);

        List<String> queries = parseLogBody(build, writer, filePath, logFileLocation, linesInLog, logger);
        List<LogObject> responses = new ArrayList<LogObject>();
        for (String query : matchedRules) {
            logger.info("Karthik: Query" + query);
            responses.addAll(getStackOverflowResponse(query));
        }

        // Write parsed output, links, etc.
        // writeLogBody();

        // Close html footer
        if (this.preformattedHtml)
            writer.write("</pre>");
        writer.write(LogParserConsts.getHtmlClosingTags());
        writer.close(); // Close to unlock and flush to disk.

        ((BufferedWriter) writers.get(LogParserConsts.ERROR)).close();
        ((BufferedWriter) writers.get(LogParserConsts.WARNING)).close();
        ((BufferedWriter) writers.get(LogParserConsts.INFO)).close();

        // Build the reference html from the warnings/errors/info html files
        // created in the loop above
        LogParserWriter.writeReferenceHtml(buildRefPath, headerForSection, statusCountPerSection, displayConstants.getIconTable(),
                displayConstants.getLinkListDisplay(), displayConstants.getLinkListDisplayPlural(), statusCount, linkFiles);
        // Write the wrapping html for the reference page and the parsed log page
        LogParserWriter.writeWrapperHtml(buildWrapperPath, responses);

        final String buildUrlPath = build.getUrl(); // job/cat_log/58
        final String buildActionPath = LogParserAction.getUrlNameStat(); // "taf_support";
        final String parsedLogURL = buildUrlPath + buildActionPath + "/log.html";

        // Create result class
        final LogParserResult result = new LogParserResult();
        result.setHtmlLogFile(parsedFilePath);
        result.setTotalErrors((Integer) statusCount.get(LogParserConsts.ERROR));
        result.setTotalWarnings((Integer) statusCount.get(LogParserConsts.WARNING));
        result.setTotalInfos((Integer) statusCount.get(LogParserConsts.INFO));
        result.setErrorLinksFile(errorLinksFilePath);
        result.setWarningLinksFile(warningLinksFilePath);
        result.setInfoLinksFile(infoLinksFilePath);
        result.setParsedLogURL(parsedLogURL);
        result.setHtmlLogPath(logDirectory);
        result.setBadParsingRulesError(this.compiledPatternsPlusError.getError());

        logger.info("Karthik: Parsed Logged File is" + parsedLogURL);
        logger.info("Karthik: Starting Parsing" + parsedFilePath);
        logger.info("Karthik: Starting Parsing" + logDirectory);

        return result;

    }

    public List<LogObject> getStackOverflowResponse(final String query) {
    	logger.info("Search query is: "+query);
    	//TODO
    	StackOverflowServiceImpl service = new StackOverflowServiceImpl();
        List<LogObject> response = null;
		try {
			response = service.jasonParser(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return response;
    }

    // public String parseLine(final String line) throws IOException {
    // return parseLine(line, null);
    // }

    public String parseLine(final String line, final String status) throws IOException {
        String parsedLine = line;
        String effectiveStatus = status;
        if (status == null) {
            effectiveStatus = LogParserConsts.NONE;
        } else if (status.equals(LogParserConsts.START)) {
            effectiveStatus = LogParserConsts.INFO;
        }

        // need to strip out for display also (in addition to parsing).
        parsedLine = ConsoleNote.removeNotes(parsedLine);
        // Allows < to be seen in log which is html
        parsedLine = parsedLine.replaceAll("<", "&lt;");
        // Allows > to be seen in log which is html
        parsedLine = parsedLine.replaceAll(">", "&gt;");

        if (effectiveStatus != null && !effectiveStatus.equals(LogParserConsts.NONE)) {
        	addtoMatchedSet(parsedLine);
            // Increment count of the status
            incrementCounter(effectiveStatus);
            incrementCounterPerSection(effectiveStatus, sectionCounter);
            // Color line according to the status
            final String parsedLineColored = colorLine(parsedLine, effectiveStatus);

            // Mark line and add to left side links of highlighted lines
            final String parsedLineColoredAndMarked = addMarkerAndLink(parsedLineColored, effectiveStatus, status);
            parsedLine = parsedLineColoredAndMarked;
            
        }
        final StringBuffer result = new StringBuffer(parsedLine);
        if (!preformattedHtml)
            result.append("<br/>\n");
        return result.toString();
    }

    /**
	 * @param parsedLine
	 */
	private void addtoMatchedSet(String parsedLine) {
		String match = parsedLine.replaceAll("&gt;", "").replaceAll("&lt;", "");
		if(!match.startsWith("+")){
			logger.info("Added to list of matches: "+match);
			matchedRules.add(match);
		}
		
	}

	public void incrementCounter(final String status) {
        final int currentVal = (Integer) statusCount.get(status);
        statusCount.put(status, currentVal + 1);
    }

    public void incrementCounterPerSection(final String status, final int sectionNumber) {
        final String key = LogParserUtils.getSectionCountKey(status, sectionNumber);
        Integer currentValInteger = (Integer) statusCountPerSection.get(key);
        // No value - entered yet - initialize with 0
        if (currentValInteger == null) {
            currentValInteger = new Integer(0);
        }
        final int newVal = currentValInteger + 1;
        statusCountPerSection.put(key, newVal);
    }

    private String colorLine(final String line, final String status) {
        final String color = (String) displayConstants.getColorTable().get(status);
        final StringBuffer result = new StringBuffer("<span style=\"color:");
        result.append(color);
        result.append("\">");
        result.append(line);
        result.append("</span>");
        return result.toString();
    }

    private String addMarkerAndLink(final String line, final String effectiveStatus, final String status) throws IOException {
        // Add marker
        final String statusCountStr = ((Integer) statusCount.get(effectiveStatus)).toString();
        final String marker = effectiveStatus + statusCountStr;

        // Add link
        final StringBuffer shortLink = new StringBuffer(" <a target=\"content\" href=\"log_content.html#");
        shortLink.append(marker);
        shortLink.append("\">");
        shortLink.append(line);
        shortLink.append("</a>");

        final StringBuffer link = new StringBuffer("<li>");
        link.append(statusCountStr);
        link.append(shortLink);
        link.append("</li><br/>");

        final BufferedWriter linkWriter = (BufferedWriter) writers.get(effectiveStatus);
        linkWriter.write(link.toString());
        linkWriter.newLine(); // Write system dependent end of line.

        // Mark the line
        final StringBuffer markedLine = new StringBuffer("<a name=\"");
        markedLine.append(marker);
        markedLine.append("\"></a>");
        markedLine.append(line);

        // Handle case where we are entering a new section
        if (status.equals(LogParserConsts.START)) {
            sectionCounter++;
            // This enters a line which will later be replaced by the actual
            // header and count for this header
            LogParserWriter.writeHeaderTemplateToAllLinkFiles(writers, sectionCounter);

            final StringBuffer brShortLink = new StringBuffer("<br/>");
            brShortLink.append(shortLink);
            headerForSection.add(brShortLink.toString());
        }

        return markedLine.toString();
    }

    private List<String> parseLogBody(
            final AbstractBuild build,
            final BufferedWriter writer,
            final FilePath filePath,
            final String logFileLocation,
            final int linesInLog,
            final Logger logger) throws IOException, InterruptedException {
        // Logging information - start

        List<String> queries = new ArrayList<String>();
        final String signature = build.getParent().getName() + "_build_" + build.getNumber();
        logger.log(Level.INFO, "LogParserParser: Start parsing : " + signature);
        final Calendar calendarStart = Calendar.getInstance();

        final LogParserStatusComputer computer = new LogParserStatusComputer(channel, filePath, parsingRulesArray, compiledPatterns, linesInLog, signature);
        final HashMap<String, String> lineStatusMatches = computer.getComputedStatusMatches();

        // Read log file from start - line by line and apply the statuses as
        // found by the threads.
        final BufferedReader reader = new BufferedReader(new FileReader(logFileLocation));
        String line;
        String status;
        int line_num = 0;
        while ((line = reader.readLine()) != null) {
            status = (String) lineStatusMatches.get(String.valueOf(line_num));
            final String parsedLine = parseLine(line, status);
            String response = parseLine(line);
            if (response != null)
                queries.add(response);
            // This is for displaying sections in the links part
            writer.write(parsedLine);
            writer.newLine(); // Write system dependent end of line.
            line_num++;
        }
        reader.close();

        // Logging information - end
        final Calendar calendarEnd = Calendar.getInstance();
        final long diffSeconds = (calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis()) / 1000;
        final long diffMinutes = diffSeconds / 60;
        logger.log(Level.INFO, "LogParserParser: Parsing took " + diffMinutes + " minutes (" + diffSeconds + ") seconds.");

        return queries;

    }

    public static String parseLine(String str) {
        if (str.contains("[ERROR]") && !str.startsWith("+")) {
            return str.substring(str.lastIndexOf("[ERROR]") + "[ERROR]".length());
        }
        return null;
    }

}
