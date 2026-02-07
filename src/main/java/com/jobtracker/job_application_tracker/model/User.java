package com.jobtracker.job_application_tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(name = "created_at",nullable = false,updatable = false,insertable = false)
    private LocalDateTime createdAt;

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }


}
