package com.wittypuppy.backend.attendance.entity;

import com.wittypuppy.backend.common.entity.ApprovalDocument;
import com.wittypuppy.backend.common.entity.ApprovalLine;
import com.wittypuppy.backend.common.entity.AttendanceManagement;
import com.wittypuppy.backend.common.entity.AttendanceWorkType;
import com.wittypuppy.backend.common.entity.Vacation;
import com.wittypuppy.backend.common.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name ="ATTENDANCE_EMPLOYEE")
@Table(name = "tbl_employee")
public class Employee {
    @Id
    @Column(name = "employee_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeCode;

    @Column(name = "department_code", columnDefinition = "BIGINT")
    private Long departmentCode;


    @Column(name = "employee_name", columnDefinition = "VARCHAR(100)")
    private String employeeName;


    @Column(name = "employee_join_date", columnDefinition = "DATETIME")
    private LocalDateTime employeeJoinDate;


    @Column(name="employee_assigned_code", columnDefinition = "BIGINT")
    private Long employeeAssignedCode;


}
