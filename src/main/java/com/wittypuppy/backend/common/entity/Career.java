package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_career")
public class Career {
    @Id
    @Column(name = "career_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long careerCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "career_start_time", columnDefinition = "DATETIME")
    private LocalDateTime careerStartTime;

    @Column(name = "career_end_time", columnDefinition = "DATETIME")
    private LocalDateTime careerEndTime;

    @Column(name = "career_position", columnDefinition = "VARCHAR(100)")
    private String careerPosition;

    @Column(name = "career_company_name", columnDefinition = "VARCHAR(100)")
    private String careerCompanyName;

    @Column(name = "career_business_information", columnDefinition = "VARCHAR(500)")
    private String careerBusinessInformation;
}
