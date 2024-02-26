package com.wittypuppy.backend.board.entity;

import com.wittypuppy.backend.group.entity.GroupEmp;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "BOARD_DEPARTMENT")
@Table(name = "tbl_department")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Department {

    @Id
    @Column(name = "department_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentCode;

    @Column(name = "department_name")
    private String departmentName;

//    @OneToMany(mappedBy = "department")
//    private List<GroupEmp> employee;

    @Column(name = "parent_department_code")
    private Long parentDepartmentCode;

}
