package com.wittypuppy.backend.approval.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_DEPARTMENT")
@Table(name = "tbl_employee")
public class Department {
    @Id
    @Column(name = "department_code")
    private Long departmentCode;

    @Column(name="department_name")
    private String departmentName;
}
