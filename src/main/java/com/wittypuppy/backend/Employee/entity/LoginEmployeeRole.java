package com.wittypuppy.backend.Employee.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

/*이게 사원테이블이랑 권한테이블의 다대다 관계를 해소시키는 해소테이블이다. */

@Entity(name = "LOGIN_EMPLOYEE_ROLE")
@Table(name = "tbl_emp_role")
@IdClass(EmployeeRolePK.class)
@Getter
@ToString
public class LoginEmployeeRole {

    @Id
    @Column(name = "employee_code", nullable = false)
    private int employeeCode;

    @Id
    @Column(name = "authority_code", nullable = false)
    private int authorityCode;

    /* Authority 타입의 속성은 조회할 때 Join용으로는 쓰지만 insert나 update할 때는 무시하라고 설정 */
    @ManyToOne
    @JoinColumn(name = "authority_code", insertable = false, updatable = false)
    private LoginRole authority;

    protected LoginEmployeeRole() {
    }

    public LoginEmployeeRole(int memberNo, int authorityCode) {
        this.employeeCode = memberNo;
        this.authorityCode = authorityCode;
    }

    public LoginEmployeeRole(LoginEmployeeRole employee, LoginRole authority) {
        this.employeeCode = employee.getEmployeeCode();
        this.authorityCode = authority.getAuthorityCode();
        this.authority = authority;
    }
    public LoginEmployeeRole(int memberNo, int authorityCode, LoginRole authority) {
        this.employeeCode = memberNo;
        this.authorityCode = authorityCode;
        this.authority = authority;

    }

    public LoginEmployeeRole memberNo(int memberNo) {
        this.employeeCode = memberNo;
        return this;
    }

    public LoginEmployeeRole authorityCode(int authorityCode) {
        this.authorityCode = authorityCode;
        return this;
    }

    public LoginEmployeeRole authority(LoginRole authority) {
        this.authority = authority;
        return this;
    }


    public LoginEmployeeRole build() {
        return new LoginEmployeeRole(employeeCode, authorityCode, authority);
    }
}
