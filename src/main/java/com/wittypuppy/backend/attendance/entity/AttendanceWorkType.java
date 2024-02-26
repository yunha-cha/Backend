package com.wittypuppy.backend.attendance.entity;

import com.wittypuppy.backend.attendance.dto.AttendanceWorkTypeDTO;
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
    @OneToOne(fetch = FetchType.LAZY)
    private Employee workTypeEmployeeCode;

    @Column(name = "attendance_work_type_status", columnDefinition = "VARCHAR(100)")
    private String attendanceWorkTypeStatus;

    @JoinColumn(name = "attendance_management_code")
    @OneToOne(fetch = FetchType.LAZY)
    private AttendanceManagement attendanceManagementCode;

    @JoinColumn(name = "approval_document_code")
    @OneToOne
    private ApprovalDocument attendanceWorkTypeApprovalDocumentCode;



}
