package com.wittypuppy.backend.group.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "GROUP_DEPARTMENT")
@Table(name = "tbl_employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupEmp {

    @Id
    @Column(name = "employee_code")
    private Long empCode;

    @Column(name = "department_code")
    private Long deptCode;

    @Column(name = "employee_name")
    private String empName;

    @Column(name = "employee_phone")
    private Long phone;


}

