package com.wittypuppy.backend.mypage.dto;

import com.wittypuppy.backend.group.entity.GroupDept;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MyPageUpdateDTO {

    private Long empCode;

    private String empName;

    private GroupDept department;

    private String empEmail;

    private String phone;

    private Date retirementDate;

    private Date empBirth;

    private String address;

    private String empPwd;

    private String empId;

    private String newEmpPwd;

    public MyPageUpdateDTO(Long empCode, String empName, GroupDept department, String empEmail, String phone, Date retirementDate, Date empBirth, String address, String empPwd, String empId, String newEmpPwd) {
        this.empCode = empCode;
        this.empName = empName;
        this.department = department;
        this.empEmail = empEmail;
        this.phone = phone;
        this.retirementDate = retirementDate;
        this.empBirth = empBirth;
        this.address = address;
        this.empPwd = empPwd;
        this.empId = empId;
        this.newEmpPwd = newEmpPwd;
    }
}
