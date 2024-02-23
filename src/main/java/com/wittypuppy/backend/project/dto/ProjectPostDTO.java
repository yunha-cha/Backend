package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectPostDTO {
    private Long projectPostCode;

    private Long projectCode;

    private Date projectPostCreationDate;

    private String projectPostContent;

    private String projectPostType;

    private String projectPostTitle;

    private List<ProjectPostFileDTO> projectPostFileList;

    private ProjectMemberDTO projectMember;
}
