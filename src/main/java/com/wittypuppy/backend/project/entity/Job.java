package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "PROJECT_JOB")
@Table(name = "tbl_job")
public class Job {
    @Id
    @Column(name = "job_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobCode;

    @Column(name="job_name")
    private String jobName;
}
