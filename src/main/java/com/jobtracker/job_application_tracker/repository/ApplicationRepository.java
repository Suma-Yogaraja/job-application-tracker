package com.jobtracker.job_application_tracker.repository;

import com.jobtracker.job_application_tracker.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> , JpaSpecificationExecutor<Application> {

    List<Application> findAllByUserId(Long userId);
}
