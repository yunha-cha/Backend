package com.wittypuppy.backend.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime projectDeadline;
    private String projectLockedStatus;
    private Long projectMemberCount;
}
