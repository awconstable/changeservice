package team.changeservice.service;

import team.changeservice.model.ChangeFailureRate;
import team.changeservice.model.ChangeRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChangeRequestService
    {
        ChangeRequest store(ChangeRequest changeRequest);

        Optional<ChangeRequest> get(String id);

        List<ChangeRequest> list();
        
        String delete(String id);
        
        List<ChangeRequest> listAllForApplication(String applicationId);

        List<ChangeRequest> listAllForHierarchy(String applicationId);

        List<ChangeRequest> listAllForApplication(String applicationId, Date reportingDate);
        
        ChangeFailureRate calculateChangeFailureRate(String applicationId, Date reportingDate);
    }
