package com.wittypuppy.backend.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "ATTENDANCE_DEPARTMENT")
@Table(name = "tbl_department")
public class Department {
    @Id
    @Column(name = "department_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentCode;

    @Column(name="department_name")
    private String departmentName;
}
