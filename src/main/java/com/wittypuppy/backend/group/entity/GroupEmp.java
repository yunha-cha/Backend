package com.wittypuppy.backend.group.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "department_code")
    private GroupDept department;

    @Column(name = "employee_name")
    private String empName;

    @Column(name = "employee_external_email")
    private String externalEmail;

    @Column(name = "employee_phone")
    private Long phone;


}

