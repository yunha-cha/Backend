package com.wittypuppy.backend.group.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "GROUP_EMPLOYEE")
@Table(name = "tbl_department")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GroupDept {

    @Id
    @Column(name = "department_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptCode;

    @Column(name = "department_name")
    private String deptName;

}
