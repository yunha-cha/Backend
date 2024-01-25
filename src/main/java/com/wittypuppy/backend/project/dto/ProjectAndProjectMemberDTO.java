package com.wittypuppy.backend.project.dto;

import com.wittypuppy.backend.project.entity.ProjectMember;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectAndProjectMemberDTO {
    private Long projectCode;

    private Long projectManagerCode;

    private String projectTitle;

    private String projectDescription;

    private String projectProgressStatus;

    private LocalDateTime projectDeadline;

    private String projectLockedStatus;

    private List<ProjectMemberDTO> projectMemberList;
}
