package com.jobtracker.job_application_tracker.repository;

import com.jobtracker.job_application_tracker.model.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository  extends JpaRepository<ActivityLog,Long> {
    Page<ActivityLog> findByApplicationId(Long applicationId, Pageable pageable);
    Page<ActivityLog> findByInterviewId(Long interviewId,Pageable pageable);

}
