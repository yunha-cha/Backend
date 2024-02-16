package com.wittypuppy.backend.Employee.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmployeeRoleDTO {

    private int employeeCode;

    private int authorityCode;

    private AuthorityDTO authority;

    public EmployeeRoleDTO() {
    }

    public EmployeeRoleDTO(int employeeNo, int authorityCode) {
        this.employeeCode = employeeNo;
        this.authorityCode = authorityCode;
    }

    public EmployeeRoleDTO(int employeeNo, int authorityCode, AuthorityDTO authority) {
        this.employeeCode = employeeNo;
        this.authorityCode = authorityCode;
        this.authority = authority;
    }
}
