package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectMemberAndPostMemberDTO {
    private Long projectMemberCode;

    private Long projectCode;

    private Long employeeCode;

    private String projectMemberDeleteStatus;

    private List<ProjectPostMemberDTO> projectPostMemberDTOList;
}
