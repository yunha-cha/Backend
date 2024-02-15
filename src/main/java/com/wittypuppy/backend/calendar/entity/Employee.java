package com.wittypuppy.backend.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "CALENDAR_EMPLOYEE")
@Table(name = "tbl_employee")
public class Employee {
    @Id
    @Column(name = "employee_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeCode;

    @JoinColumn(name = "department_code")
    @ManyToOne
    private Department department;

    @JoinColumn(name = "job_code")
    @ManyToOne
    private Job job;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "employee_retirement_date")
    private Date employeeRetirementDate;

    @Column(name="employee_assigned_code")
    private Long employeeAssignedCode;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<Profile> profileList;
}
