package com.wittypuppy.backend.attendance.dto;

import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class AttendanceWorkTypeDTO {

    private Long attendanceWorkTypeCode;

    private EmployeeDTO employeeCode;

    private String attendanceWorkTypeStatus;

    private ApprovalDocumentDTO workTypeApprovalDocumentCode;

    private AttendanceManagementDTO attendanceManagementCode;
}
