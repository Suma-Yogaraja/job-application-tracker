package com.jobtracker.job_application_tracker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.job_application_tracker.repository.ApplicationRepository;
import com.jobtracker.job_application_tracker.repository.InterviewRepository;
import com.jobtracker.job_application_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationIT {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    // cleanup
    @Autowired InterviewRepository interviewRepository;
    @Autowired ApplicationRepository applicationRepository;
    @Autowired UserRepository userRepository;

    @BeforeEach
    void cleanup() {
        interviewRepository.deleteAll();
        applicationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void login_returnsJwt_and_protectedEndpoint_requiresAuth() throws Exception {
        String email = "auth@test.com";
        String password = "Password@12345";

        // 1) Register
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "%s"
                                }
                                """.formatted(email, password)))
                .andExpect(status().is2xxSuccessful());

        // 2) Login -> token
        String loginBody = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "%s"
                                }
                                """.formatted(email, password)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = extractToken(loginBody);
        assertThat(token).isNotBlank();

        // 3) Protected endpoint WITHOUT token -> should fail (401/403)
        mockMvc.perform(get("/api/applications/paged"))
                .andExpect(status().is4xxClientError());

        // 4) Protected endpoint WITH token -> should succeed
        mockMvc.perform(get("/api/applications/paged")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


    private String extractToken(String responseBody) throws Exception {
        String trimmed = responseBody == null ? "" : responseBody.trim();

        // If API returns raw token string
        if (trimmed.length() > 20 && !trimmed.startsWith("{")) {
            return trimmed.replace("\"", "");
        }

        Map<String, Object> map = objectMapper.readValue(trimmed, new TypeReference<>() {});
        for (String key : new String[]{"token", "accessToken", "jwt", "access_token"}) {
            Object v = map.get(key);
            if (v instanceof String s && !s.isBlank()) return s;
        }

        throw new IllegalStateException("Login response did not contain a token. Body: " + responseBody);
    }
}
