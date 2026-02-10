package com.jobtracker.job_application_tracker.repository;

import com.jobtracker.job_application_tracker.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview,Long> {
    List<Interview> findAllByApplicationId(Long applicationId);
}
