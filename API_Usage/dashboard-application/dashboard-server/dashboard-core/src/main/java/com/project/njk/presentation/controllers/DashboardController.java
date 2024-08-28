package com.project.njk.presentation.controllers;

import com.project.njk.application.services.RequestService;
import com.project.njk.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DashboardController {

    @Autowired
    RequestService requestService;

    @RequestMapping(value = "/api/greeting", method = RequestMethod.GET)
    public String greeting() {
        return "{\"value\":\"HELLO\"}";
    }

    @RequestMapping(value = "/api/apiid", method = RequestMethod.GET)
    public int apiId(@RequestParam(value = "apiValue") String apiValue) {
        return requestService.getApiId(apiValue);
    }

    @RequestMapping(value = "/api/countannotatedmethodsbyapi", method = RequestMethod.GET)
    public int countAnnotatedMethodsByApi(@RequestParam(value = "apiValue") String apiValue) {
        return requestService.countAnnotatedMethodsByApi(apiValue);
    }

    @RequestMapping(value = "/api/api", method = RequestMethod.GET)
    public List<API> getAllApi() {
        return requestService.getAllApi();
    }

    @RequestMapping(value = "/api/annotatedmethods", method = RequestMethod.GET)
    public List<AnnotatedMethod> getAllAnnotatedMethods() {
        return requestService.getAllAnnotatedMethods();
    }

    @RequestMapping(value = "/api/listTafClasses", method = RequestMethod.GET)
    public List<AnnotatedClass> getAllAnnotatedClasses(@RequestParam(value = "apiValue") String apiValue) {
        return requestService.getAllAnnotatedClasses(apiValue);
    }

    @RequestMapping(value = "/api/listTeamClasses", method = RequestMethod.GET)
    public List<TeamClass> getTeamClassesByApi(@RequestParam(value = "apiValue") String apiValue) {
        return requestService.getTeamClassesByApi(apiValue);
    }

    @RequestMapping(value = "/api/listTeamMethods", method = RequestMethod.GET)
    public List<TeamMethod> getTeamMethodsByApi(@RequestParam(value = "apiValue") String apiValue) {
        return requestService.getTeamMethodsByApi(apiValue);
    }
}
