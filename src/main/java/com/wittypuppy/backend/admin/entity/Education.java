package com.wittypuppy.backend.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name="ADMIN_EDUCATION")
@Table(name="tbl_education")
@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class Education {
    @Id
    @Column(name="education_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationCode;

//    @ManyToOne
    @Column(name="employee_code")
    private Long employeeCode;

    @Column(name="education_graduate_date")
    private Date educationGraduateDate;
    @Column(name="education_admission_date")
    private Date educationAdmissionDate;
    @Column(name="education_major")
    private String educationMajor;
    @Column(name="education_name")
    private String educationName;
    @Column(name="education_grades")
    private Double educationGrades;
}
