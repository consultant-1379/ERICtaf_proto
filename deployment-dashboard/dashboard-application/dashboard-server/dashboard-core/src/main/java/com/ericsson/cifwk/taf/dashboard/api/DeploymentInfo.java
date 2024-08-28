
package com.ericsson.cifwk.taf.dashboard.api;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.HostType;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;
import com.ericsson.nms.host.HostConfigurator;
import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeploymentInfo {

    public static final String INST_LOCATION_FOLDER = "/opt/ericsson/enminst/bin/";
    public static final String ENM_VERSION_SCRIPT = "enm_version.sh";
    public static final String VCS_SCRIPT = "vcs.bsh";
    public static final String TAF_CLUSTER_ID = "taf.clusterId";
    public static final String ROUND_BRACKETS = "\\((.*?)\\)";

    public static Logger logger = Logger.getLogger(DeploymentInfo.class);

    static CLICommandHelper commandHelper;

    public static String getENMVersion(String clusterId) {
        DataHandler.getConfiguration().setProperty(TAF_CLUSTER_ID, clusterId);
        if (checkMSConnectionStatus()) {
            commandHelper = new CLICommandHelper(HostConfigurator.getMS());
        }
        String versionOutput = commandHelper.execute(INST_LOCATION_FOLDER + ENM_VERSION_SCRIPT + " | grep -i \"ENM Version\"");
        commandHelper.disconnect();
        if (!versionOutput.contains("ISO Version:")) {
            return "Not Available";
        } else {
            Pattern pattern = Pattern.compile(ROUND_BRACKETS);
            Matcher match = pattern.matcher(versionOutput);
            match.find();
            return match.group(1).split(":")[1];
        }
    }

    public static String retrieveStatusOfServices(String clusterId) {
        DataHandler.getConfiguration().setProperty(TAF_CLUSTER_ID, clusterId);
        if (checkMSConnectionStatus()) {
            commandHelper = new CLICommandHelper(HostConfigurator.getMS());
        }
        String serviceStatusOutput = commandHelper.execute(INST_LOCATION_FOLDER + VCS_SCRIPT + " --groups");
        return serviceStatusOutput;
    }

    public static Map<String, String> retrieveStatusOfHosts(String clusterId) {
        Map<String, String> hosts = new HashMap<>();
        DataHandler.getConfiguration().setProperty(TAF_CLUSTER_ID, clusterId);
        List<Host> listOfHosts = new ArrayList();

        Host ms = HostConfigurator.getMS();
        if (!checkMSConnectionStatus()) {
            hosts.put(ms.getHostname(), convertReachable(false));
            return hosts;
        } else {
            hosts.put(ms.getHostname(), convertReachable(true));
        }

        listOfHosts.addAll(HostConfigurator.getSVCNodes());
        listOfHosts.addAll(HostConfigurator.getDbNodes());

        for (Host host : listOfHosts) {
            if (host == null) {
                continue;
            }
            hosts.put(host.getHostname(), convertReachable(checkConnectionStatus(host)));
        }

        return hosts;
    }

    public static boolean checkMSConnectionStatus() {
        boolean reachable = false;
        try {
            reachable = InetAddress.getByName(HostConfigurator.getMS().getIp()).isReachable(2000);
            return reachable;
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return reachable;
    }

    public static boolean checkConnectionStatus(Host host) {
        boolean reachable = false;
        try {
            reachable = InetAddress.getByAddress(host.getIp().getBytes()).isReachable(2000);
            return reachable;
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return reachable;
    }

    public static String convertReachable(boolean value) {
        if (value) {
            return "ONLINE";
        } else {
            return "OFFLINE";
        }
    }

}
