package com.wittypuppy.backend.project.dto.viewProjectInfo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectDTO {
    private Long projectCode;

    private EmployeeDTO projectManager;

    private String projectTitle;

    private String projectDescription;

    private String projectProgressStatus;

    private LocalDateTime projectDeadline;

    private String projectLockedStatus;

    private List<ProjectPostDTO> projectPostDTOList;

    private List<ProjectMemberDTO> projectMemberDTOList;
}
