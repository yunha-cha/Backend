package com.wittypuppy.backend.admin.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class EducationDTO {
    private Long employeeCode;
    private Double educationGrade;
    private String educationAdmissionDate;
    private String educationGraduateDate;
    private String educationMajor;
    private String educationName;
}
