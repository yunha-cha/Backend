package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_work_type")
public class WorkType {
    @Id
    @Column(name = "work_type_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workTypeCode;

    @Column(name = "approval_document_code", columnDefinition = "BIGINT")
    private Long approvalDocumentCode;

    @Column(name = "work_type_form", columnDefinition = "VARCHAR(100)")
    private String workTypeForm;

    @Column(name = "work_type_title", columnDefinition = "VARCHAR(100)")
    private String workTypeTitle;

    @Column(name = "work_type_start_date", columnDefinition = "DATETIME")
    private LocalDateTime workTypeStartDate;

    @Column(name = "work_type_end_date", columnDefinition = "DATETIME")
    private LocalDateTime workTypeEndDate;

    @Column(name = "work_type_place", columnDefinition = "VARCHAR(100)")
    private String workTypePlace;

    @Column(name = "work_type_reason", columnDefinition = "VARCHAR(3000)")
    private String workTypeReason;
}
