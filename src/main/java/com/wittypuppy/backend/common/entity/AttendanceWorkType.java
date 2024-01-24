package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_attendance_work_type")
public class AttendanceWorkType {
    @Id
    @Column(name = "attendance_work_type_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceWorkTypeCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "attendance_work_type_status", columnDefinition = "VARCHAR(100)")
    private String attendanceWorkTypeStatus;

    @Column(name = "approval_document_code", columnDefinition = "BIGINT")
    private Long approvalDocumentCode;

    @Column(name = "attendance_management_code", columnDefinition = "BIGINT")
    private Long attendanceManagementCode;
}
