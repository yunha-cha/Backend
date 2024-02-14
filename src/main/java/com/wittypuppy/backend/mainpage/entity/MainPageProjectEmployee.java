package com.wittypuppy.backend.mainpage.entity;

import com.wittypuppy.backend.project.entity.Department;
import com.wittypuppy.backend.project.entity.Job;
import com.wittypuppy.backend.project.entity.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "MAINPAGE_PROJECT_EMPLOYEE")
@Table(name = "tbl_employee")
public class MainPageProjectEmployee {

    @Id
    @Column(name = "employee_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeCode;

    @Column(name="employee_name")
    private String employeeName;

    @Column(name="employee_retirement_date")
    private LocalDateTime employeeRetirementDate;

}
