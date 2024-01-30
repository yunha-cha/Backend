package com.wittypuppy.backend.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name ="ATTENDANCE_WORK_TYPE")
@Table(name = "tbl_attendance_work_type")
public class AttendanceWorkType {
    @Id
    @Column(name = "attendance_work_type_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceWorkTypeCode;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee workTypeEmployeeCode;

    @Column(name = "attendance_work_type_status", columnDefinition = "VARCHAR(100)")
    private String attendanceWorkTypeStatus;

    @JoinColumn(name = "attendance_management_code")
    @OneToOne
    private AttendanceManagement attendanceManagementCode;

    @JoinColumn(name = "approval_document_code")
    @OneToMany
    private List<ApprovalDocument> attendanceWorkTypeApprovalDocumentCode;
}
