package com.wittypuppy.backend.admin.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class CareerDTO {
    private Long careerCode;
    private Long employeeCode;
    private Date careerStartDate;
    private Date careerEndDate;
    private String careerCompanyName;
    private String careerPosition;
    private String careerBusinessInformation;
}
