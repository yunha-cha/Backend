package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
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
    private Date projectPostCreationDate;
    private Date projectPostModifyDate;
    private Date projectPostDueDate;
    private String employeeName;
    private String employeeDeptName;
    private Long projectPostMemberCount;
}
