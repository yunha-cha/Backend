package com.wittypuppy.backend.approval.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_WORK_TYPE")
@Table(name = "tbl_work_type")
public class WorkType {
    @Id
    @Column(name = "work_type_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workTypeCode;

    @JoinColumn(name = "approval_document_code")
    @ManyToOne
    private ApprovalDoc approvalDoc;

    @Column(name = "work_type_form")
    private String workTypeForm;

    @Column(name = "work_type_title")
    private String workTypeTitle;

    @Column(name = "work_type_start_date")
    private LocalDateTime workTypeStartDate;

    @Column(name = "work_type_end_date")
    private LocalDateTime workTypeEndDate;

    @Column(name = "work_type_place")
    private String workTypePlace;

    @Column(name = "work_type_reason")
    private String workTypeReason;
}
