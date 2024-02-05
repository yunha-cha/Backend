package com.wittypuppy.backend.approval.dto;
import com.wittypuppy.backend.calendar.dto.DepartmentDTO;
import com.wittypuppy.backend.calendar.dto.JobDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class EmployeeDTO {
    private Long employeeCode;
    private DepartmentDTO departmentDTO;
    private JobDTO jobDTO;
    private String employeeName;
    private Long employeeAssignedCode;
    private Long onLeaveCount;
}
