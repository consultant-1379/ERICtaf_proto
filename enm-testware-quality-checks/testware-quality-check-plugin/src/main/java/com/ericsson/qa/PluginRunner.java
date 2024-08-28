package com.ericsson.qa;

import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import static org.twdata.maven.mojoexecutor.MojoExecutor.ExecutionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 07/10/2016
 */
class PluginRunner {

    private final Log logger;
    private final ExecutionEnvironment pluginEnv;

    PluginRunner(Log logger, ExecutionEnvironment pluginEnv) {
        this.logger = logger;
        this.pluginEnv = pluginEnv;
    }

    void run(Plugin plugin, String executionName, Xpp3Dom pluginConfig, String... goals) throws MojoExecutionException {
        logger.info(String.format("[%S STARTED]", executionName));
        long startTime = System.currentTimeMillis();
        try {
            for (String goal : goals) {
                executeMojo(plugin, goal(goal), pluginConfig, pluginEnv);
            }
        } finally {
            logger.info(String.format("[%S FINISHED] - took %d millis", executionName, (System.currentTimeMillis() - startTime)));
        }
    }

}
