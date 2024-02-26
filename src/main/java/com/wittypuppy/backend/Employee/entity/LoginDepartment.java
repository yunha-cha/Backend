package com.wittypuppy.backend.Employee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name = "LOGIN_DEPARTMENT")
@Table(name = "tbl_department")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginDepartment {
    @Id
    @Column(name="department_code")
    private Long departmentCode;

    @Column(name="parent_department_code")
    private Long parentDepartmentCode;

    @Column(name="department_name")
    private String departmentName;
}
