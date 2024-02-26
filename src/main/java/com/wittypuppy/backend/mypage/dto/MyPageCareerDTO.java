package com.wittypuppy.backend.mypage.dto;

import com.wittypuppy.backend.mypage.entity.MyPageEmp;

import lombok.*;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyPageCareerDTO {

    private Long careerCode;
    private Long employeeCode;
    private String careerStartDate;
    private String careerEndDate;
    private String careerCompanyName;
    private String careerPosition;
    private String careerBusinessInformation;
}
