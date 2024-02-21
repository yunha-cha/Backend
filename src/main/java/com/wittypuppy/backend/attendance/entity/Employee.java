package com.wittypuppy.backend.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private int employeeCode;

    @JoinColumn(name = "department_code")
    @ManyToOne
    private Department employeeDepartmentCode;


    @Column(name = "employee_name", columnDefinition = "VARCHAR(100)")
    private String employeeName;


    @Column(name = "employee_join_date", columnDefinition = "DATETIME")
    private LocalDateTime employeeJoinDate;


    @Column(name="employee_assigned_code", columnDefinition = "BIGINT")
    private Long employeeAssignedCode;

}
