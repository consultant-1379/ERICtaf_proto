package com.ericsson.cifwk.taf.grid;

import com.ericsson.cifwk.taf.grid.web.WebApplication;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

/**
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        // options.
        // options.addOption()

        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse(options, args);

        startWebServer();
    }

    public static void startWebServer() throws Exception {
        WebApplication application = new WebApplication();
        application.init();
    }

}
