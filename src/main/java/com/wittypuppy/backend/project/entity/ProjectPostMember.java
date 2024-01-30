package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
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
    @OneToMany(cascade = CascadeType.ALL)
    private List<ProjectPostComment> projectPostCommentList;
}
