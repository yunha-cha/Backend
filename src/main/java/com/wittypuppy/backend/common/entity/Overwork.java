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
@Table(name = "tbl_overwork")
public class Overwork {
    @Id
    @Column(name = "ovework_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oveworkCode;

    @Column(name = "approval_document_code", columnDefinition = "BIGINT")
    private Long approvalDocumentCode;

    @Column(name = "overwork_title", columnDefinition = "VARCHAR(100)")
    private String overworkTitle;

    @Column(name = "kind_of_overwork", columnDefinition = "VARCHAR(100)")
    private String kindOfOverwork;

    @Column(name = "overwork_date", columnDefinition = "DATETIME")
    private LocalDateTime overworkDate;

    @Column(name = "overwork_start_time", columnDefinition = "DATETIME")
    private LocalDateTime overworkStartTime;

    @Column(name = "overwork_end_time", columnDefinition = "DATETIME")
    private LocalDateTime overworkEndTime;

    @Column(name = "overwork_reason", columnDefinition = "VARCHAR(3000)")
    private String overworkReason;
}
