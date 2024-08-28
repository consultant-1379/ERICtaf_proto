package com.ericsson.oss.cite.getters;

import com.ericsson.cifwk.taf.data.DataHandler;

public class EnvironmentSetUpGetter {

    private static String configFiles;
    private static String hostName;

    private static String getConfigFiles() {
        if (configFiles == null)
            configFiles = DataHandler.getAttribute("CONFIG_FILES").toString();
        return configFiles;

    }

    private static String getHostName() {
        if (hostName == null)
            hostName = DataHandler.getHostByName("gateway").getIp();
        return hostName;
    }

    public static String getInitialInstallCommand() {
        return "/export/scripts/CLOUD/bin/master.sh -c " + getConfigFiles() + " -g " + getHostName() + " -o yes -l /export/scripts/CLOUD/logs/web/CI_EXEC_OSSRC/ -f rollout_config";
    }

    public static String getArneImportCommand() {
        return "/export/scripts/CLOUD/bin/master.sh -c " + getConfigFiles() + "  -g " + getHostName() + "  -o yes -l /export/scripts/CLOUD/logs/web/CI_EXEC_OSSRC/ -f netsim_post_steps";
    }

    public static String getAddUserCommand() {
        return "/export/scripts/CLOUD/bin/master.sh -c " + getConfigFiles() + " -g " + getHostName() + " -o yes -l /export/scripts/CLOUD/logs/web/CI_EXEC_OSSRC/ -f create_users_config";

    }
}
