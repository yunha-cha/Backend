package com.wittypuppy.backend.project.dto;

import com.wittypuppy.backend.project.entity.Employee;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ProjectMemberDTO {
    private Long projectMemberCode;

    private Long projectCode;

    private Employee employeeCode;

    private String projectMemberDeleteStatus;

    private List<ProjectPostMemberDTO> projectPostMemberDTOList;
}