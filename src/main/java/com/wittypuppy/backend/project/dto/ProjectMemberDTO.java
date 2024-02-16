package com.wittypuppy.backend.project.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectMemberDTO {
    private Long projectMemberCode;

    private Long projectCode;

    private EmployeeDTO employee;

    private String projectMemberDeleteStatus;
}
