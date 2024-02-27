package com.wittypuppy.backend.mypage.dto;

import com.wittypuppy.backend.mypage.entity.MyPageEmp;
import lombok.*;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyPageEducationDTO {


    private Long employeeCode;
    private Double educationGrade;
    private String educationAdmissionDate;
    private String educationGraduateDate;
    private String educationMajor;
    private String educationName;
}
