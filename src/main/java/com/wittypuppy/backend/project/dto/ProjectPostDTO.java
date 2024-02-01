package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ProjectPostDTO {
    private Long projectPostCode;

    private Long projectCode;

    private String projectPostStatus;

    private String projectPostPriority;

    private String projectPostTitle;

    private LocalDateTime projectPostCreationDate;

    private LocalDateTime projectPostModifyDate;

    private LocalDateTime projectPostDueDate;

    private List<ProjectPostMemberDTO> projectPostMemberDTOList;

    private List<ProjectPostCommentDTO> projectPostCommentDTOList;
}
