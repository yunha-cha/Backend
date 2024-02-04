package com.wittypuppy.backend.attendance.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class AttendanceWorkTypeDTO {

    private Long attendanceWorkTypeCode;

    private EmployeeDTO employeeCode;

    private String attendanceWorkTypeStatus;

    private List<ApprovalDocumentDTO> workTypeApprovalDocumentCode;

    private AttendanceManagementDTO attendanceManagementCode;


}
