package com.wittypuppy.backend.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name="ADMIN_CAREER")
@Table(name="tbl_career")
public class Career {
    @Id
    @Column(name="career_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long careerCode;

    @Column(name="employee_code")
    private Long employeeCode;

    @Column(name="career_start_date")
    private Date careerStartDate;
    @Column(name="career_end_date")
    private Date careerEndDate;
    @Column(name="career_company_name")
    private String careerCompanyName;
    @Column(name="career_position")
    private String careerPosition;
    @Column(name="career_business_information")
    private String careerBusinessInformation;
}
