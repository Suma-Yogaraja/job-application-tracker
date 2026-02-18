package com.jobtracker.job_application_tracker.repository.spec;

import com.jobtracker.job_application_tracker.model.Application;
import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import org.springframework.data.jpa.domain.Specification;

public final class ApplicationSpecifications {
    private ApplicationSpecifications() {}

    public static Specification<Application> belongsToUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Application> hasStatus(ApplicationStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Application> companyContains(String company) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("company")), "%" + company.toLowerCase() + "%");
    }

    public static Specification<Application> roleContains(String role) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("role")), "%" + role.toLowerCase() + "%");
    }
}
