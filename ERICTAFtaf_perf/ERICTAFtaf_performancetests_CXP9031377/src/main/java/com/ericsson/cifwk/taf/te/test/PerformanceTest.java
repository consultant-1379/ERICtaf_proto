package com.ericsson.cifwk.taf.te.test;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.cifwk.taf.tools.http.HttpToolBuilder;
import org.testng.annotations.Test;


public class PerformanceTest extends TorTestCaseHelper {

    @Test
    public void shouldPass() {
        Host serverHost = DataHandler.getHostByName("JenkinsMaster");
        HttpTool tool = HttpToolBuilder.newBuilder(serverHost)
                .build();
        tool.get("/jenkins");
//        System.out.println(httpResponse.getStatusLine());
    }

}
