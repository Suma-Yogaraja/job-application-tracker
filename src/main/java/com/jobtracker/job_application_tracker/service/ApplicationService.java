package com.jobtracker.job_application_tracker.service;


import com.jobtracker.job_application_tracker.dto.ApplicationResponse;
import com.jobtracker.job_application_tracker.dto.CreateApplicationRequest;
import com.jobtracker.job_application_tracker.dto.UpdateStatusRequest;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

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
        //map to dto
        return toResponse(app);
    }


    public List<ApplicationResponse> getMyApplications(String emaill){
        User user=userRepository.findByEmail(emaill)
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
            app.setStatus(status);
        Application saved=applicationRepository.save(app);
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


