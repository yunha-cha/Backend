package com.wittypuppy.backend.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectOptionsDTO {
    private String projectTitle;
    private String projectDescription;
    private String projectProgressStatus;
    private Date projectDeadline;
    private String projectLockedStatus;
}
