package com.project.njk.application.services;

import com.project.njk.application.repositories.RequestRepository;
import com.project.njk.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ekiajen on 16/06/2016.
 */
@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public int getApiId(String apiName) {
        return requestRepository.getApiId(apiName);
    }

    public int countAnnotatedMethodsByApi(String apiName) {
        long apiId = getApiId(apiName);
        return requestRepository.countAnnotatedMethodsByApi(apiId);
    }

    public List<API> getAllApi() {
        return requestRepository.getAllApi();
    }

    public List<AnnotatedMethod> getAllAnnotatedMethods() {
        return requestRepository.getAllAnnotatedMethods();
    }

    public List<AnnotatedClass> getAllAnnotatedClasses(String apiValue) {
        return requestRepository.getAnnotatedClassByApi(apiValue);
    }

    public List<TeamClass> getTeamClassesByApi(String apiValue) {
        return requestRepository.getTeamClassesByApi(apiValue);
    }

    public List<TeamMethod> getTeamMethodsByApi(String apiValue) {
        return requestRepository.getTeamMethodsByApi(apiValue);
    }

}
