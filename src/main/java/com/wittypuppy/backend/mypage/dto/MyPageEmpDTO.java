package com.wittypuppy.backend.mypage.dto;
import com.wittypuppy.backend.admin.dto.CareerDTO;
import com.wittypuppy.backend.admin.dto.EducationDTO;
import com.wittypuppy.backend.mypage.entity.MyPageEducation;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
//@Setter
@ToString

public class MyPageEmpDTO {


private Long empCode;

private MyPageDeptDTO department;

private String empName;

private String empEmail;

private String phone;

private Date empBirth;

private Date empJoinDate;

private String empAddress;

private MyPageJobDTO job;

    private List<MyPageEducationDTO> education;

    private List<MyPageCareerDTO> career;


    public MyPageEmpDTO setEmpCode(Long empCode) {
        this.empCode = empCode;
        return this;
    }

    public MyPageEmpDTO setDepartment(MyPageDeptDTO department) {
        this.department = department;
        return this;
    }

    public MyPageEmpDTO setEmpName(String empName) {
        this.empName = empName;
        return this;
    }

    public MyPageEmpDTO setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
        return this;
    }

    public MyPageEmpDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public MyPageEmpDTO setEmpBirth(Date empBirth) {
        this.empBirth = empBirth;
        return this;
    }

    public MyPageEmpDTO setEmpJoinDate(Date empJoinDate) {
        this.empJoinDate = empJoinDate;
        return this;
    }

    public MyPageEmpDTO setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
        return this;
    }

    public MyPageEmpDTO setJob(MyPageJobDTO job) {
        this.job = job;
        return this;
    }

    public void setEducation(List<MyPageEducationDTO> education) {
        this.education = education;
    }

    public void setCareer(List<MyPageCareerDTO> career) {
        this.career = career;
    }
}

