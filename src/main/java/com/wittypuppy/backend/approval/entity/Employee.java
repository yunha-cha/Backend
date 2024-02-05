package com.wittypuppy.backend.approval.entity;

import com.wittypuppy.backend.calendar.entity.Job;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_EMPLOYEE")
@Table(name = "tbl_employee")
public class Employee {
    @Id
    @Column(name = "employee_code")
    private Long employeeCode;

    @JoinColumn(name = "department_code")
    @ManyToOne
    private Department department;

    @JoinColumn(name="job_code")
    @ManyToOne
    private Job job;

    @Column(name="employee_name")
    private String employeeName;

    @Column(name = "employee_assigned_code")
    private Long employeeAssignedCode;

    @Column(name = "employee_on_leave_count")
    private Long onLeaveCount;
}
