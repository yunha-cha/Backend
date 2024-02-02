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
}
