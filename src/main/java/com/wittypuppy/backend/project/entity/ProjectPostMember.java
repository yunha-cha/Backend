package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity(name = "PROJECT_PROJECT_POST_MEMBER")
@Table(name = "tbl_project_post_member")
public class ProjectPostMember {
    @Id
    @Column(name = "project_post_member_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectPostMemberCode;

    @Column(name = "project_post_code")
    private Long projectPostCode;

    @Column(name = "project_member_code")
    private Long projectMemberCode;

    @Column(name = "project_post_member_delete_status")
    private String projectPostMemberDeleteStatus;

    @JoinColumn(name = "project_post_member_code")
    @OneToMany
    private List<ProjectPostComment> projectPostCommentList;

    public ProjectPostMember setProjectPostMemberCode(Long projectPostMemberCode) {
        this.projectPostMemberCode = projectPostMemberCode;
        return this;
    }

    public ProjectPostMember setProjectPostCode(Long projectPostCode) {
        this.projectPostCode = projectPostCode;
        return this;
    }

    public ProjectPostMember setProjectMemberCode(Long projectMemberCode) {
        this.projectMemberCode = projectMemberCode;
        return this;
    }

    public ProjectPostMember setProjectPostMemberDeleteStatus(String projectPostMemberDeleteStatus) {
        this.projectPostMemberDeleteStatus = projectPostMemberDeleteStatus;
        return this;
    }

    public ProjectPostMember setProjectPostCommentList(List<ProjectPostComment> projectPostCommentList) {
        this.projectPostCommentList = projectPostCommentList;
        return this;
    }

    public ProjectPostMember builder() {
        return new ProjectPostMember(projectPostMemberCode, projectPostCode, projectMemberCode, projectPostMemberDeleteStatus, projectPostCommentList);
    }
}
