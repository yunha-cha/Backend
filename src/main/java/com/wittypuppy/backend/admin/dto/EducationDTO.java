package com.wittypuppy.backend.admin.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
public class EducationDTO {
    private Long employeeCode;
    private Double educationGrades;
    private Date educationAdmissionDate;
    private Date educationGraduateDate;
    private String educationMajor;
    private String educationName;
}
