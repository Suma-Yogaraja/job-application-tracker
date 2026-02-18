package com.jobtracker.job_application_tracker;

import com.jobtracker.job_application_tracker.model.Application;
import com.jobtracker.job_application_tracker.model.ApplicationStatus;
import com.jobtracker.job_application_tracker.model.User;
import com.jobtracker.job_application_tracker.repository.ApplicationRepository;
import com.jobtracker.job_application_tracker.repository.InterviewRepository;
import com.jobtracker.job_application_tracker.repository.UserRepository;
import com.jobtracker.job_application_tracker.security.JwtService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ApplicationOwnershipIT {

    @Autowired MockMvc mockMvc;
    @Autowired InterviewRepository interviewRepository;
    @Autowired UserRepository userRepository;
    @Autowired ApplicationRepository applicationRepository;
    @Autowired JwtService jwtService;



    @Test
    void pagedEndpoint_returnsOnlyCurrentUsersApplications() throws Exception {
        // User A
        User userA = userRepository.save(new User(null, "a@test.com", "hashA", null));

        Application a1 = new Application();
        a1.setUser(userA);
        a1.setCompany("Google");
        a1.setRole("Software Engineer");
        a1.setStatus(ApplicationStatus.INTERVIEWING);
        a1.setNotes("A's application");
        applicationRepository.save(a1);

        // User B
        User userB = userRepository.save(new User(null, "b@test.com", "hashB", null));

        Application b1 = new Application();
        b1.setUser(userB);
        b1.setCompany("Amazon");
        b1.setRole("SDE");
        b1.setStatus(ApplicationStatus.APPLIED);
        b1.setNotes("B's application");
        applicationRepository.save(b1);

        String tokenForUserA = jwtService.generateToken("a@test.com");

        mockMvc.perform(get("/api/applications/paged")
                        .header("Authorization", "Bearer " + tokenForUserA)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].company").value("Google"))
                .andExpect(jsonPath("$.items[0].notes").value("A's application"));
    }
}
