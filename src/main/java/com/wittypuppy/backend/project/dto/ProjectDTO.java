package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ProjectDTO {
    private Long projectCode;

    private EmployeeDTO projectManager;

    private String projectTitle;

    private String projectDescription;

    private String projectProgressStatus;

    private LocalDateTime projectDeadline;

    private String projectLockedStatus;

    private List<ProjectMemberDTO> projectMemberDTOList;

    private List<ProjectPostDTO> projectPostDTOList;
}
