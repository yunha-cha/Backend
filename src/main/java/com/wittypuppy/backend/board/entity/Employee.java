package com.wittypuppy.backend.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "BOARD_EMPLOYEE")
@Table(name = "tbl_employee")
public class Employee {

    @Id
    @Column(name = "employee_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeCode;

    @Column(name = "employee_name",columnDefinition = "VARCHAR(100)")
    private String employeeName;


}
