package com.jobtracker.job_application_tracker.service;


import com.jobtracker.job_application_tracker.dto.CreateInterviewRequest;
import com.jobtracker.job_application_tracker.dto.InterviewResponse;
import com.jobtracker.job_application_tracker.dto.InterviewScheduledEvent;
import com.jobtracker.job_application_tracker.messaging.InterviewScheduledProducer;
import com.jobtracker.job_application_tracker.model.Application;
import com.jobtracker.job_application_tracker.model.Interview;
import com.jobtracker.job_application_tracker.model.User;
import com.jobtracker.job_application_tracker.repository.ApplicationRepository;
import com.jobtracker.job_application_tracker.repository.InterviewRepository;
import com.jobtracker.job_application_tracker.repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final UserRepository userRepository;
    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewScheduledProducer interviewScheduledProducer;

    public InterviewResponse createInterview(Long applicationID, CreateInterviewRequest req,String email){


        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("user not found"));
        Application app=applicationRepository.findById(applicationID)
                .orElseThrow(()->new RuntimeException("application not found"));

        if(!app.getUser().getId().equals(user.getId()))
                throw new RuntimeException("forbidden");

        Interview interview=new Interview();
        interview.setApplication(app);
        interview.setRoundType(req.getRoundType());
        interview.setScheduledAt(req.getScheduledAt());
        interview.setNotes(req.getNotes());
        Interview saved=interviewRepository.save(interview);

        InterviewScheduledEvent event=new InterviewScheduledEvent();
        event.setInterviewId((saved.getId()));
        event.setCreatedAt(LocalDateTime.now());
        event.setApplicationId(saved.getApplication().getId());
        event.setRoundType(saved.getRoundType());
        event.setScheduledAt(saved.getScheduledAt());

        interviewScheduledProducer.publish(event);

        return toResponse(saved);
    }

    public List<InterviewResponse> listInterviews(Long applicationID,String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("user not found"));
        Application app=applicationRepository.findById(applicationID)
                .orElseThrow(()->new RuntimeException("application not found"));

        if (!app.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("forbidden");
        }

        List<Interview> interviews=interviewRepository.findAllByApplicationId(applicationID);
        List<InterviewResponse> res=new ArrayList<>();
        for(Interview i : interviews){
            res.add(toResponse(i));
        }
        return res;
    }
    private InterviewResponse toResponse(Interview interview) {
        InterviewResponse res = new InterviewResponse();
        res.setId(interview.getId());
        res.setRoundType(interview.getRoundType());
        res.setScheduledAt(interview.getScheduledAt());
        res.setNotes(interview.getNotes());
        return res;
    }

}
