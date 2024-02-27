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
@Entity(name = "MYPAGE_EDUCATION")
@Table(name = "tbl_education")
public class MyPageEducation {
    @Id
    @Column(name="education_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationCode;

    @Column(name="employee_code")
    private Long employeeCode;

    @Column(name="education_graduate_date")
    private String educationGraduateDate;
    @Column(name="education_admission_date")
    private String educationAdmissionDate;
    @Column(name="education_major")
    private String educationMajor;
    @Column(name="education_name")
    private String educationName;
    @Column(name="education_grades")
    private Double educationGrades;
}
