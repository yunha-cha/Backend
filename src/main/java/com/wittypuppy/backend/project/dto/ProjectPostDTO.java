package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectPostDTO {
    private Long projectCode;
    private String projectPostStatus;
    private String projectPostPriority;
    private String projectPostTitle;
    private LocalDateTime projectPostDueDate;
}
