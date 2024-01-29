package com.wittypuppy.backend.project.entity;

import com.wittypuppy.backend.common.entity.Project;
import com.wittypuppy.backend.common.entity.ProjectMember;
import com.wittypuppy.backend.common.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity(name = "PROJECT_EMPLOYEE")
@Table(name = "tbl_employee")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Employee {


    @Id
    @Column(name = "employee_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeCode;

    @Column(name = "department_code", columnDefinition = "BIGINT")
    private Long departmentCode;

}
