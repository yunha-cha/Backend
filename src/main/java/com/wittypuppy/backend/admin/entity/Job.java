package com.wittypuppy.backend.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name="ADMIN_JOB")
@Table(name="tbl_job")
public class Job {
    @Id
    @Column(name="job_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobCode;

    @Column(name="job_name")
    private String jobName;
}
