package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectDTO {
    private Long projectCode;
    private Long projectManagerCode;
    private String projectTitle;
    private String projectDescription;
    private String projectProgressStatus;
    private LocalDateTime projectDeadline;
    private String projectLockedStatus;
}
