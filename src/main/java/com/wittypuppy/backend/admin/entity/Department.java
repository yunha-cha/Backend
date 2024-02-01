package com.wittypuppy.backend.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="ADMIN_DEPARTMENT")
@Table(name="tbl_department")
@AllArgsConstructor
@NoArgsConstructor
@Setter@Getter
public class Department {
    @Id
    @Column(name="department_code")
    private Long departmentCode;

    @Column(name="parent_department_code")
    private Long parentDepartmentCode;

    @Column(name="department_name")
    private String departmentName;
}
