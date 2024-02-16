package com.wittypuppy.backend.group.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity(name = "GROUP_EMPLOYEE")
@Table(name = "tbl_employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupEmp {

    @Id
    @Column(name = "employee_code")
    private Long empCode;

    @Column(name = "employee_name")
    private String empName;

    @ManyToOne
    @JoinColumn(name = "department_code")
    private GroupDept department;

    @Column(name = "employee_external_email")
    private String empEmail;

    @Column(name = "employee_phone")
    private String phone;

    @Column(name = "employee_retirement_date")
    private Date retirementDate;

    @ManyToOne
    @JoinColumn(name = "job_code")
    private GroupJob job;

}

