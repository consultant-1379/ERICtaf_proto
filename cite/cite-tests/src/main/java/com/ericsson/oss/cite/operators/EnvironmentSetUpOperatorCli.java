package com.ericsson.oss.cite.operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.cifwk.taf.tools.cli.CLITool;
import com.ericsson.cifwk.taf.tools.cli.Shell;
import com.ericsson.cifwk.taf.tools.cli.jsch.JSchCLIToolException;
import com.ericsson.oss.cite.getters.EnvironmentSetUpGetter;

@Operator(context = { Context.CLI })
public class EnvironmentSetUpOperatorCli implements EnvironmentSetUpOperator {

    private static final CLI cli = new CLI(DataHandler.getHostByName("gateway"));

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentSetUpOperatorCli.class);

    private static final String EXIT_CODE = "EXIT_CODE:";

    private boolean runCommand(String command, Long timeOut) {
        logger.debug("Running {} cmd", command);
        Shell runShell = cli.executeCommand(command, "echo \"" + EXIT_CODE + "\"$?");

        String output = null;
        while (!runShell.isClosed() && timeOut > 0) {
            output = runShell.read();
            logger.debug(output);
            timeOut -= CLITool.DEFAULT_TIMEOUT_SEC;
        }
        String lastLine = runShell.read();
        short result = -1;
        try {
            if (lastLine.length() < EXIT_CODE.length())
                lastLine = output;
            String exitCode = lastLine.split(EXIT_CODE)[1].trim();
            result = Short.valueOf(exitCode);
        } catch (JSchCLIToolException | IndexOutOfBoundsException e) {
            logger.error("Cannot get command result", e);
        }
        logger.info("Shell exit code is " + result);
        runShell.disconnect();
        return result == 0;

    }

    @Override
    public boolean executeInitialInstall() {
        return runCommand(EnvironmentSetUpGetter.getInitialInstallCommand(), 32400L);
    }

    @Override
    public boolean executeArneImport() {
        return runCommand(EnvironmentSetUpGetter.getArneImportCommand(), 14400L);
    }

    @Override
    public boolean prepareUsers() {
        return runCommand(EnvironmentSetUpGetter.getAddUserCommand(), 3600L);
    }

}
