package com.wittypuppy.backend.approval.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_SOFTWARE_USE")
@Table(name = "tbl_software_use")
public class SoftwareUse {
    @Id
    @Column(name = "software_use_code")
    private Long softwareUseCode;

    @JoinColumn(name = "approval_document_code")
    @ManyToOne
    private ApprovalDoc approvalDoc;

    @Column(name = "software_title")
    private String softwareTitle;

    @Column(name = "kind_of_software")
    private String kindOfSoftware;

    @Column(name = "software_reason")
    private String softwareReason;

    @Column(name = "software_start_date")
    private LocalDateTime softwareStartDate;
}
