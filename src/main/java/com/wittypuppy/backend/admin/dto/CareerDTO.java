package com.wittypuppy.backend.admin.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class CareerDTO {
    private Long careerCode;
    private Long employeeCode;
    private String careerStartDate;
    private String careerEndDate;
    private String careerCompanyName;
    private String careerPosition;
    private String careerBusinessInformation;
}
