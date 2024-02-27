package com.wittypuppy.backend.mypage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "MYPAGE_JOB")
@Table(name = "tbl_job")
public class MyPageJob {
    @Id
    @Column(name = "job_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobCode;

    @Column(name = "job_name")
    private String jobName;


}
