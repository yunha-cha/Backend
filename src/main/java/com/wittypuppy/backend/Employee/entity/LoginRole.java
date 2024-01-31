package com.wittypuppy.backend.Employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Entity(name = "LOGIN_ROLE")
@Table(name = "tbl_role")
@AllArgsConstructor
@Getter
@ToString
public class LoginRole {

    @Id
    @Column(name = "authority_code", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authorityCode;

    @Column(name = "authority_name", length = 255, nullable = false)
    private String authorityName;

    @Column(name = "authority_desc", length = 4000, nullable = false)
    private String authorityDesc;

    protected LoginRole() {}

    public LoginRole authorityCode(int authorityCode) {
        this.authorityCode = authorityCode;
        return this;
    }

    public LoginRole authorityName(String authorityName) {
        this.authorityName = authorityName;
        return this;
    }

    public LoginRole authorityDesc(String authorityDesc) {
        this.authorityDesc = authorityDesc;
        return this;
    }

    public LoginRole build() {
        return new LoginRole(authorityCode, authorityName, authorityDesc);
    }
}
