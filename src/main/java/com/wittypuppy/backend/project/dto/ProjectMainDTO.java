package com.wittypuppy.backend.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ProjectMainDTO {
    private Long projectCode;
    private String projectManagerName;
    private String projectManagerDeptName;
    private String projectTitle;
    private String projectDescription;
    private String projectProgressStatus;
    private Date projectDeadline;
    private String projectLockedStatus;
    private Long projectMemberCount;
}
