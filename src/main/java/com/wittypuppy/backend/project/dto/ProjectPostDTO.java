package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectPostDTO {
    private Long projectCode;
    private Long projectPostCode;
    private String projectPostStatus;
    private String projectPostPriority;
    private String projectPostTitle;
    private LocalDateTime projectPostCreationDate;
    private LocalDateTime projectPostModifyDate;
    private LocalDateTime projectPostDueDate;
    private String employeeName;
    private String employeeDeptName;
    private Long projectPostMemberCount;
}
