package com.wittypuppy.backend.project.entity.viewProjectInfo;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name="PROJECT_VIEW_PROJECT_INFO_PROJECT_MEMBER")
@Table(name = "tbl_project_member")
public class ProjectMember {
    @Id
    @Column(name = "project_member_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMemberCode;

    @Column(name = "project_code", columnDefinition = "BIGINT")
    private Long projectCode;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee projectMember;

    @Column(name = "project_member_delete_status", columnDefinition = "VARCHAR(1)")
    private String projectMemberDeleteStatus;
}
