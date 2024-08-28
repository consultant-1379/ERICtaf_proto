package com.ericsson.cifwk.ve.plugins.jira;

import com.ericsson.cifwk.ve.infrastructure.config.Settings;
import com.ericsson.cifwk.ve.infrastructure.config.SettingsProvider;
import com.ericsson.cifwk.ve.plugins.jira.dto.JiraResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rcarz.jiraclient.JiraException;

/**
 *
 */
public class JiraDemo {

    public static void main(String[] args) throws Exception {
        SettingsProvider provider = new SettingsProvider();
        Settings settings = provider.loadSettings();

        Jira jira = new Jira(
                settings.getString("jira.username"),
                settings.getString("jira.password"),
                settings.getString("jira.uri"),
                settings.getString("jira.fields.storyPoints")
        );
        jira.connect();
        getEpic(jira, "CIP-3727");
        getEpic(jira, "error");
    }

    private static void getEpic(Jira jira, String epicId) throws JiraException, JsonProcessingException {
        JiraResponse response = jira.getEpicStructure(epicId);
        System.out.println(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(response);
        System.out.println(json);
    }

}
