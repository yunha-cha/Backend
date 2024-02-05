package com.wittypuppy.backend.mypage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wittypuppy.backend.group.entity.GroupDept;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "MYPAGE_UPDATE_EMPLOYEE")
@Table(name = "tbl_employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@ToString
public class MyPageUpdateEmp {

    @Id
    @Column(name = "employee_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empCode;

    @Column(name = "employee_name")
    private String empName;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Column(name = "employee_address")
    private String address;

    @Column(name = "employee_id")
    private String empId;

    @Column(name = "employee_password")
    private String empPwd;



    //이게 빌더 패턴이다
    public MyPageUpdateEmp empCode(Long empCode) {
        this.empCode = empCode;
        return this;
    }

    public MyPageUpdateEmp empName(String empName) {
        this.empName = empName;
        return this;
    }

    public MyPageUpdateEmp department(GroupDept department) {
        this.department = department;
        return this;
    }

    public MyPageUpdateEmp empEmail(String empEmail) {
        this.empEmail = empEmail;
        return this;
    }

    public MyPageUpdateEmp Phone(String phone) {
        this.phone = phone;
        return this;
    }

    public MyPageUpdateEmp retirementDate(Date retirementDate) {
        this.retirementDate = retirementDate;
        return this;
    }

    public MyPageUpdateEmp empBirth(Date empBirth) {
        this.empBirth = empBirth;
        return this;
    }

    public MyPageUpdateEmp address(String address) {
        this.address = address;
        return this;
    }

    public MyPageUpdateEmp empId(String empId) {
        this.empId = empId;
        return this;
    }

    public MyPageUpdateEmp empPwd(String empPwd) {
        this.empPwd = empPwd;
        return this;
    }

    public MyPageUpdateEmp build() {
        return new MyPageUpdateEmp(empCode, empName , department, empEmail, phone, retirementDate, empBirth, address,empId,empPwd);
    }


}

