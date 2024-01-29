package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "PROJECT_PROJECT_POST_COMMENT")
@Table(name = "tbl_project_post_comment")
public class ProjectPostComment {
    @Id
    @Column(name = "project_post_comment_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectPostCommentCode;

    @Column(name = "project_post_code")
    private Long projectPostCode;

    @Column(name = "project_post_comment_content")
    private Long projectPostCommentContent;

    @Column(name = "project_post_comment_creation_date")
    private Long projectPostCommentCreationDate;

    @Column(name = "project_post_member_code")
    private Long projectPostMemberCode;

    @JoinColumn(name="project_post_comment_code")
    @OneToMany
    private List<ProjectPostCommentFile> projectPostCommentFileList;
}
