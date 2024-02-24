package com.wittypuppy.backend.group.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "GROUP_JOB")
@Table(name = "tbl_job")
public class GroupJob {


    @Id
    @Column(name = "job_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobCode;

    @Column(name = "job_name")
    private String jobName;



}
