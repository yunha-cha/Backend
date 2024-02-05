package com.wittypuppy.backend.approval.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_JOB")
@Table(name = "tbl_job")
public class Job {
    @Id
    @Column(name = "job_code")
    private Long jobCode;

    @Column(name="job_name")
    private String jobName;
}
