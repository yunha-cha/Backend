package com.wittypuppy.backend.project.entity;

import com.wittypuppy.backend.common.entity.ProjectPostComment;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name="PROJECT_PROJECT_POST_MEMBER")
@Table(name = "tbl_project_post_member")
public class ProjectPostMember {
    @Id
    @Column(name = "project_post_member_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectPostMemberCode;

    @Column(name = "project_post_code", columnDefinition = "BIGINT")
    private Long projectPostCode;

    @Column(name = "project_member_code", columnDefinition = "BIGINT")
    private Long projectMemberCode;

    @Column(name="project_post_member_delete_status", columnDefinition = "VARCHAR(1)")
    private String projectPostMemberDeleteStatus;
}
