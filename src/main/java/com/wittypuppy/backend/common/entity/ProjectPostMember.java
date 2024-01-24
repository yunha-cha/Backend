package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
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
}
