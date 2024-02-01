package com.wittypuppy.backend.project.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ProjectPostMemberDTO {
    private Long projectPostMemberCode;

    private Long projectPostCode;

    private Long projectMemberCode;

    private String projectPostMemberDeleteStatus;

    private List<ProjectPostCommentDTO> projectPostCommentDTOList;
}
