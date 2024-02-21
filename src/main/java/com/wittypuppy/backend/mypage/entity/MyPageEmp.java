package com.wittypuppy.backend.mypage.entity;

import com.wittypuppy.backend.group.entity.GroupDept;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity(name = "MYPAGE_EMPLOYEE")
@Table(name = "tbl_employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyPageEmp {

    @Id
    @Column(name = "employee_code")
    private Long empCode;

    @Column(name = "employee_name")
    private String empName;

    @ManyToOne
    @JoinColumn(name = "department_code")
    private GroupDept department;

    @Column(name = "employee_external_email")
    private String empEmail;

    @Column(name = "employee_phone")
    private String phone;

    @Column(name = "employee_retirement_date")
    private Date retirementDate;

    @Column(name = "employee_birth_date")
    private Date empBirth;

    @Column(name = "employee_join_date")
    private Date empJoinDate;

    @Column(name = "employee_address")
    private String empAddress;


//    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<MyPageProfile> profiles;




}

