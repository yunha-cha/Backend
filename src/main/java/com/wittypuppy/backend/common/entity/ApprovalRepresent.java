package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_approval_represent")
public class ApprovalRepresent {
    @Id
    @Column(name = "approval_represent_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalRepresentCode;

    @Column(name = "approval_document_code", columnDefinition = "BIGINT")
    private Long approvalDocumentCode;

    @ManyToOne
    @JoinColumn(name = "representative_code",columnDefinition = "BIGINT")
    private Employee approvalRepresentativeEmployee;

    @ManyToOne
    @JoinColumn(name="assignee_code",columnDefinition = "BIGINT")
    private Employee approvalAssigneeEmployee;
}
