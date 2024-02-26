package com.wittypuppy.backend.mypage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "MYPAGE_CAREER")
@Table(name = "tbl_career")
public class MyPageCareer {
    @Id
    @Column(name="career_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long careerCode;

    @Column(name="employee_code")
    private Long employeeCode;

    @Column(name="career_start_date")
    private String careerStartDate;
    @Column(name="career_end_date")
    private String careerEndDate;
    @Column(name="career_company_name")
    private String careerCompanyName;
    @Column(name="career_position")
    private String careerPosition;
    @Column(name="career_business_information")
    private String careerBusinessInformation;
}
