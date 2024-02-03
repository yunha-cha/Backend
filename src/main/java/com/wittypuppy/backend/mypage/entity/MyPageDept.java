package com.wittypuppy.backend.mypage.entity;

import com.wittypuppy.backend.group.entity.GroupEmp;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "MYPAGE_DEPARTMENT")
@Table(name = "tbl_department")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MyPageDept {

    @Id
    @Column(name = "department_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptCode;

    @Column(name = "department_name")
    private String deptName;

    @OneToMany(mappedBy = "department")
    private List<GroupEmp> employee;

}
