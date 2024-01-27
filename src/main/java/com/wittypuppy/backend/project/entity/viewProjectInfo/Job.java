package com.wittypuppy.backend.project.entity.viewProjectInfo;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name="PROJECT_VIEW_PROJECT_INFO_JOB")
@Table(name = "tbl_job")
public class Job {
    @Id
    @Column(name = "job_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobCode;

    @Column(name = "job_name", columnDefinition = "VARCHAR(100)")
    private String jobName;
}
