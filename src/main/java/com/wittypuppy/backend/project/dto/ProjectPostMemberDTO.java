package com.wittypuppy.backend.project.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectPostMemberDTO {
    private Long projectPostMemberCode;
    private Long projectPostCode;
    private Long projectMemberCode;
    private String projectPostMemberDeleteStatus;
    private Long employeeCode;
    private String employeeName;
    private String profileFileURL;
    private String departmentName;
    private String jobName;
}
