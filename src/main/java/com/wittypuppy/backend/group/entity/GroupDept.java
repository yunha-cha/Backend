package com.wittypuppy.backend.group.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "GROUP_DEPARTMENT")
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

    @OneToMany(mappedBy = "department")
    private List<GroupEmp> employee;

}
