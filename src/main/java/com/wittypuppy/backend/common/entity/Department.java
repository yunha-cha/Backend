package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_department")
public class Department {
    @Id
    @Column(name = "department_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentCode;

    @Column(name = "department_name", columnDefinition = "VARCHAR(100)")
    private String departmentName;

    @ManyToOne
    @JoinColumn(name="parent_department_code",columnDefinition = "BIGINT")
    private Department parentDepartment;

    @OneToMany(mappedBy = "parentDepartment")
    private List<Department> childDepartmentList;

    @JoinColumn(name = "department_code")
    @OneToMany
    private List<Event> eventList;

    @JoinColumn(name = "department_code")
    @OneToMany
    private List<Employee> employeeList;
}
