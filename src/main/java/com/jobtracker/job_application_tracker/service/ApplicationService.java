package com.jobtracker.job_application_tracker.service;


import com.jobtracker.job_application_tracker.dto.CreateApplicationRequest;
import com.jobtracker.job_application_tracker.model.Application;
import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import com.jobtracker.job_application_tracker.model.User;
import com.jobtracker.job_application_tracker.repository.ApplicationRepository;
import com.jobtracker.job_application_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public Application create(CreateApplicationRequest req,String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("user not found"));

        //build application
        Application app=new Application();
        app.setCompany(req.getCompany());
        app.setUser(user);
        app.setStatus(ApplicationStatus.APPLIED);
        return applicationRepository.save(app);
    }

    public List<Application> getMyApplication(String emaill){
        User user=userRepository.findByEmail(emaill)
                .orElseThrow(()-> new RuntimeException("user not found"));
        return applicationRepository.findAllByUserId(user.getId());
    }

}


