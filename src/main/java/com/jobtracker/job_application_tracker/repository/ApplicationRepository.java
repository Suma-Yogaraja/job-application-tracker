package com.jobtracker.job_application_tracker.repository;

import com.jobtracker.job_application_tracker.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

    List<Application> findAllByUserId(Long userId);
}
