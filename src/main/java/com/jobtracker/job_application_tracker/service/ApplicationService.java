package com.jobtracker.job_application_tracker.service;


import com.jobtracker.job_application_tracker.dto.*;
import com.jobtracker.job_application_tracker.exception.ForbiddenException;
import com.jobtracker.job_application_tracker.exception.NotFoundException;
import com.jobtracker.job_application_tracker.messaging.ApplicationCreatedProducer;
import com.jobtracker.job_application_tracker.messaging.StatusChangedProducer;
import com.jobtracker.job_application_tracker.model.Application;
import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import com.jobtracker.job_application_tracker.model.User;
import com.jobtracker.job_application_tracker.repository.ApplicationRepository;
import com.jobtracker.job_application_tracker.repository.UserRepository;
import com.jobtracker.job_application_tracker.repository.spec.ApplicationSpecifications;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final StatusChangedProducer statusChangedProducer;
    private final ApplicationCreatedProducer applicationCreatedProducer;

    public ApplicationResponse create(CreateApplicationRequest req, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("user not found"));

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
                .orElseThrow(()-> new NotFoundException("user not found"));
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
                .orElseThrow(()->new NotFoundException("user not found"));
            Application app=applicationRepository.findById(applicationID)
                .orElseThrow(()->new NotFoundException("application Not found"));
            if (!app.getUser().getId().equals(user.getId())) {
                    throw new ForbiddenException("forbidden");
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

private  Long getUserIdByEmail(String email){
        User user=userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("user not found") );
        return user.getId();
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

    public Page<ApplicationResponse> getMyApplicationsPaged(String email, String status, String company, String role, int page, int size, String sort) {

        Long userId=getUserIdByEmail(email);
        Pageable pageable= PageRequest.of(
                Math.max(page,0),
                clamp(size,1,50),
                parseSort(sort)
        );
        Specification<Application> spec = Specification
                .where(ApplicationSpecifications.belongsToUserId(userId));

        if (status != null && !status.isBlank()) {
            ApplicationStatus st = ApplicationStatus.valueOf(status.trim().toUpperCase());
            spec = spec.and(ApplicationSpecifications.hasStatus(st));
        }
        if (company != null && !company.isBlank()) {
            spec = spec.and(ApplicationSpecifications.companyContains(company.trim()));
        }
        if (role != null && !role.isBlank()) {
            spec = spec.and(ApplicationSpecifications.roleContains(role.trim()));
        }

        return applicationRepository.findAll(spec, pageable)
                .map(this::toResponse); // your existing mapper

    }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("createdAt", "appliedDate", "company", "role", "status");

    private Sort parseSort(String sortParam) {
        String field = "createdAt";
        Sort.Direction direction = Sort.Direction.DESC;

        if (sortParam != null && !sortParam.isBlank()) {
            String[] parts = sortParam.split(",");
            if (parts.length >= 1 && !parts[0].isBlank()) {
                String requested = parts[0].trim();
                if (ALLOWED_SORT_FIELDS.contains(requested)) field = requested;
            }
            if (parts.length >= 2 && !parts[1].isBlank()) {
                direction = "asc".equalsIgnoreCase(parts[1].trim()) ? Sort.Direction.ASC : Sort.Direction.DESC;
            }
        }

        return Sort.by(direction, field);
    }

}


