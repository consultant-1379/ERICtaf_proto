package com.ericsson.cifwk.taf.dashboard.presentation.controllers;

import com.ericsson.cifwk.taf.dashboard.api.dto.DeploymentRequest;
import com.ericsson.cifwk.taf.dashboard.api.RetrieveData;
import com.ericsson.cifwk.taf.dashboard.infrastructure.ThreadManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;


@RestController
public class DashboardController {

    private SseEmitter sseEmitter;

    @RequestMapping(value = "/api/stream", method = RequestMethod.GET)
    public SseEmitter getEmitter() {
        sseEmitter = new SseEmitter(-1L);
        sseEmitter.onCompletion(new Runnable() {
            @Override
            public void run() {
                ThreadManager.removeAll();
            }
        });

        return sseEmitter;
    }

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody DeploymentRequest deploymentRequest) {
        ThreadGroup previousThreads = ThreadManager.get(deploymentRequest.getUid());
        if (previousThreads != null) {
            ThreadManager.removeItem(deploymentRequest.getUid());
        }

        ThreadGroup threadGroup = new ThreadGroup (deploymentRequest.getUid());

        RetrieveData.getNodeServices(sseEmitter, deploymentRequest, threadGroup, 30000L);
        RetrieveData.getNodeVersion(sseEmitter, deploymentRequest, threadGroup, 30000L);
        RetrieveData.getPingableNodes(sseEmitter, deploymentRequest, threadGroup, 30000L);

        ThreadManager.put(deploymentRequest.getUid(), threadGroup);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/unregister/{uid}", method = RequestMethod.POST)
    public ResponseEntity unregister(@PathVariable("uid") String uid) {
        ThreadGroup threads = ThreadManager.get(uid);
        if (threads != null) {
            ThreadManager.removeItem(uid);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }
}
