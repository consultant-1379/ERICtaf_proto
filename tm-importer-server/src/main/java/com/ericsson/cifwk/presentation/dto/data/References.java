package com.ericsson.cifwk.presentation.dto.data;

import com.ericsson.cifwk.infrastructure.PropertyLoader;
import com.ericsson.cifwk.infrastructure.RestClient;
import com.ericsson.cifwk.presentation.dto.ReferenceData;
import com.ericsson.cifwk.presentation.dto.ReferenceDataItem;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by egergle on 10/03/2015.
 */
public class References {

    public static final String REF_PATH = "referencesPath";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String CLIENT_URL = "clientUrl";

    public static final String PROJECT = "project";

    public static final String GROUP = "group";
    public static final String COMPONENT = "component";
    public static final String PRIORITY = "priority";
    public static final String CONTEXT = "context";
    public static final String TYPE = "type";
    public static final String TEST_CASE_STATUS = "testCaseStatus";
    public static final String EXECUTION_TYPE = "executionType";

    private Map<String, String> group = new HashMap();
    private Map<String, String> component = new HashMap();
    private Map<String, String> executionType = new HashMap();
    private Map<String, String> context = new HashMap();
    private Map<String, String> type = new HashMap();
    private Map<String, String> testCaseStatus = new HashMap();
    private Map<String, String> priority = new HashMap();
    private Map<String, String> priorityInReverse = new HashMap();


    private static final GenericType<List<ReferenceData>> REFERENCE_DATA_LIST =
            new GenericType<List<ReferenceData>>() {
            };

    public References() {
        RestClient restClient = new RestClient(PropertyLoader.getProperty(CLIENT_URL).toString());
        restClient.login(PropertyLoader.getProperty(USER).toString(), PropertyLoader.getProperty(PASSWORD).toString());

        String[] supportedReferences = new String[]{
                GROUP,
                PRIORITY,
                CONTEXT,
                TEST_CASE_STATUS,
                TYPE,
                EXECUTION_TYPE
        };

        Response response = restClient.path(PropertyLoader.getProperty(REF_PATH).toString())
                .queryParam("referenceId", (Object[]) supportedReferences)
                .queryParam("projectId", PropertyLoader.getProperty(PROJECT).toString())
                .request()
                .get();

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new NullPointerException();
        }

        List<ReferenceData> referenceData = response.readEntity(REFERENCE_DATA_LIST);


       for (ReferenceData item : referenceData) {
           if (item.getId().equals(GROUP)) {
               group = translateOptions(item, group);
           } else if(item.getId().equals(COMPONENT)) {
               component = translateOptions(item, component);
           } else if(item.getId().equals(CONTEXT)) {
               context = translateOptions(item, context);
           } else if(item.getId().equals(TYPE)) {
               type = translateOptions(item, type);
           } else if(item.getId().equals(PRIORITY)) {
               priority = translateOptions(item, priority);
               priorityInReverse = translateOptionsReverse(item, priorityInReverse);
           } else if(item.getId().equals(TEST_CASE_STATUS)) {
               testCaseStatus = translateOptions(item, testCaseStatus);
           } else if(item.getId().equals(EXECUTION_TYPE)) {
               executionType = translateOptions(item, executionType);
           }
        }

        restClient.logout();
    }

    private Map<String, String> translateOptions(ReferenceData item, Map<String, String> data) {
        for (ReferenceDataItem refDataItem : item.getItems()) {
            data.put(refDataItem.getTitle(), refDataItem.getId());
        }
        return data;
    }

    private Map<String, String> translateOptionsReverse(ReferenceData item, Map<String, String> data) {
        for (ReferenceDataItem refDataItem : item.getItems()) {
            data.put(refDataItem.getId(), refDataItem.getTitle());
        }
        return data;
    }

    public Map<String, String> getGroup() {
        return group;
    }
    public Map<String, String> getComponent() {
        return component;
    }

    public Map<String, String> getExecutionType() {
        return executionType;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public Map<String, String> getType() {
        return type;
    }

    public Map<String, String> getPriority() {
        return priority;
    }

    public Map<String, String> getPriorityInReverse() {
        return priorityInReverse;
    }
}
