package com.wittypuppy.backend.project.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectPostCommentDTO {
    private Long projectPostCode;
    private String projectPostCommentContent;
    private Long projectPostMemberCode;
    private Long projectMemberCode;
    private Long employeeCode;
    private String employeeName;
    private String departmentName;
    private String jobName;
    private String profileImageURL;
    private String projectPostCommentImageURL;
    private Long projectPostCommentImageCount;
}
