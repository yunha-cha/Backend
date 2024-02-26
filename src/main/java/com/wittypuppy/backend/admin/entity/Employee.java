package com.wittypuppy.backend.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Entity(name = "ADMIN_EMPLOYEE")
@Table(name = "tbl_employee")
public class Employee {
    @Id
    @Column(name = "employee_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeCode;

    //하나의 사원은 하나의 department만 가질 수 있다.
    //department는 여러 사원을 가질 수 있다.
    @ManyToOne
    @JoinColumn(name="department_code")
    private Department department;

    @ManyToOne
    @JoinColumn(name="job_code")
    private Job job;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "education_code")
    private List<Education> educations;
//
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "career_code")
    private List<Career> careers;

    @Column(name="employee_birth_date")
    private String employeeBirthDate;
    @Column(name="employee_join_date")
    private String employeeJoinDate;
    @Column(name="employee_address")
    private String employeeAddress;
    @Column(name="employee_id")
    private String employeeId;
    @Column(name="employee_name")
    private String employeeName;
    @Column(name="employee_password")
    private String employeePassword;
    @Column(name="employee_phone")
    private String employeePhone;
    @Column(name="employee_resident_number")
    private String employeeResidentNumber;
    @Column(name="employee_retirement_date")
    private String employeeRetirementDate;
    @Column(name="employee_assigned_code")
    private String employeeAssignedCode;
    @Column(name="employee_on_leave_count")
    private String employeeOnLeaveCount;
    @Column(name="employee_external_email")
    private String employeeExternalEmail;
    @Column(name="employee_role")
    private String employeeRole;
}
