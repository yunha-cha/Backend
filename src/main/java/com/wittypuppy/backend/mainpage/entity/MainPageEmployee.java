package com.wittypuppy.backend.mainpage.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "MAINPAGE_BOARD_EMPLOYEE")
@Table(name = "tbl_employee")
public class MainPageEmployee {

    @Id
    @Column(name = "employee_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeCode;

    @Column(name = "employee_name",columnDefinition = "VARCHAR(100)")
    private String employeeName;

}
