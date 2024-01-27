package com.wittypuppy.backend.project.entity.viewProjectInfo;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name="PROJECT_VIEW_PROJECT_INFO_PROJECT_POST_MEMBER")
@Table(name = "tbl_project_post_member")
public class ProjectPostMember {
    @Id
    @Column(name = "project_post_member_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectPostMemberCode;

    @Column(name = "project_post_code", columnDefinition = "BIGINT")
    private Long projectPostCode;

    @JoinColumn(name = "project_member_code")
    @ManyToOne
    private Employee projectMember;

    @Column(name="project_post_member_delete_status", columnDefinition = "VARCHAR(1)")
    private String projectPostMemberDeleteStatus;
}
