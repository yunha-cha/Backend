package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "PROJECT_DEPARTMENT")
@Table(name = "tbl_department")
public class Department {
    @Id
    @Column(name = "department_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentCode;

    @Column(name="department_name")
    private String departmentName;
}
