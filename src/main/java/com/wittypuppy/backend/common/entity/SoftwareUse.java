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
@Table(name = "tbl_software_use")
public class SoftwareUse {
    @Id
    @Column(name = "software_use_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long softwareUseCode;

    @Column(name = "approval_document_code", columnDefinition = "BIGINT")
    private Long approvalDocumentCode;

    @Column(name = "software_title", columnDefinition = "VARCHAR(100)")
    private String softwareTitle;

    @Column(name = "kind_of_software", columnDefinition = "VARCHAR(500)")
    private String kindOfSoftware;

    @Column(name = "software_reason", columnDefinition = "VARCHAR(3000)")
    private String softwareReason;

    @Column(name = "software_start_date", columnDefinition = "DATETIME")
    private LocalDateTime softwareStartDate;
}
