package com.jobtracker.job_application_tracker.service;


import com.jobtracker.job_application_tracker.dto.*;
import com.jobtracker.job_application_tracker.messaging.ApplicationCreatedProducer;
import com.jobtracker.job_application_tracker.messaging.StatusChangedProducer;
import com.jobtracker.job_application_tracker.model.Application;
import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import com.jobtracker.job_application_tracker.model.User;
import com.jobtracker.job_application_tracker.repository.ApplicationRepository;
import com.jobtracker.job_application_tracker.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final StatusChangedProducer statusChangedProducer;
    private final ApplicationCreatedProducer applicationCreatedProducer;

    public ApplicationResponse create(CreateApplicationRequest req, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("user not found"));

        //build application
        Application app=new Application();
        app.setCompany(req.getCompany());
        app.setRole(req.getRole());
        app.setNotes(req.getNotes());
        app.setLink(req.getLink());
        app.setAppliedDate(req.getAppliedDate());
        app.setStatus(req.getStatus() == null ? ApplicationStatus.APPLIED : req.getStatus());
        app.setUser(user);
        Application savedApp= applicationRepository.save(app);
        ApplicationCreatedEvent event=new ApplicationCreatedEvent();
        event.setCreatedAt(LocalDateTime.now());
        event.setApplicationId(savedApp.getId());
        event.setRole(savedApp.getRole());
        event.setCompany(savedApp.getCompany());

        applicationCreatedProducer.publish(event);
        //map to dto
        return toResponse(app);
    }


    public List<ApplicationResponse> getMyApplications(String email){
        User user=userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("user not found"));
        List<Application> apps= applicationRepository.findAllByUserId(user.getId());
        List<ApplicationResponse> res=new ArrayList<>();
        for(Application app:apps){
            res.add(toResponse(app));
        }
        return res;
    }

    public ApplicationResponse updateStatus(Long applicationID, ApplicationStatus status,String email){

        if(status==null)
                throw new RuntimeException("status cannot be null");
            User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("user not found"));
            Application app=applicationRepository.findById(applicationID)
                .orElseThrow(()->new RuntimeException("application not found"));
            if (!app.getUser().getId().equals(user.getId())) {
                    throw new RuntimeException("forbidden");
          }
            ApplicationStatus old=app.getStatus();
            app.setStatus(status);
            Application saved=applicationRepository.save(app);

        StatusChangedEvent event=new StatusChangedEvent();
        event.setApplicationId(saved.getId());
        event.setOldStatus(old);
        event.setNewStatus(saved.getStatus());
        event.setChangedAt(LocalDateTime.now());

        statusChangedProducer.publish(event);
        return toResponse(saved);
    }


    private ApplicationResponse toResponse(Application app) {
        ApplicationResponse res = new ApplicationResponse();
        res.setId(app.getId());
        res.setCompany(app.getCompany());
        res.setRole(app.getRole());
        res.setStatus(app.getStatus());
        res.setNotes(app.getNotes());
        res.setLink(app.getLink());
        res.setAppliedDate(app.getAppliedDate());
        return res;
    }

}


