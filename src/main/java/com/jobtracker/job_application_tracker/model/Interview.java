package com.jobtracker.job_application_tracker.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id",nullable = false)
    private Application application;

    @Enumerated(EnumType.STRING)
    @Column(name = "round_type",nullable = false)
    private InterviewRoundType roundType;

    @Column(name = "scheduled_at" ,nullable = false)
    private LocalDateTime scheduledAt;

    @Column(name = "notes")
    private String notes;


}
