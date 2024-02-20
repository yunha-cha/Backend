package com.wittypuppy.backend.attendance.dto;

import com.wittypuppy.backend.Employee.dto.User;
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

    private ApprovalDocumentDTO workTypeApprovalDocumentCode;

    private AttendanceManagementDTO attendanceManagementCode;


}
