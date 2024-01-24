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
@Table(name = "tbl_education")
public class Education {
    @Id
    @Column(name = "education_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "education_name", columnDefinition = "VARCHAR(100)")
    private String educationName;

    @Column(name = "education_major", columnDefinition = "VARCHAR(100)")
    private String educationMajor;

    @Column(name = "education_grades", columnDefinition = "FLOAT")
    private Double educationGrades;

    @Column(name = "education_admission_date", columnDefinition = "DATETIME")
    private LocalDateTime educationAdmissionDate;

    @Column(name = "education_graduate_date", columnDefinition = "DATETIME")
    private LocalDateTime educationGraduateDate;
}
