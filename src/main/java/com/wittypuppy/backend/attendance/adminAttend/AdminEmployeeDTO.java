package com.wittypuppy.backend.attendance.adminAttend;

import com.wittypuppy.backend.attendance.dto.DepartmentDTO;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdminEmployeeDTO {

    private Long adminEmployeeCode;

    private DepartmentDTO employeeAdminDepartmentCode;

    private String employeeNameAdmin;

    private LocalDateTime employeeJoinDateAdmin;

    private String employeeRole;

    private LocalDateTime vacationCreationDate;

    private LocalDateTime vacationExpirationDate;

    private String vacationCreationReason;
}
