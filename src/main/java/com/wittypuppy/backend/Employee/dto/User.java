package com.wittypuppy.backend.Employee.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User implements UserDetails {

    private int employeeCode;
    private String employeeId;
    private String employeePassword;
    private String employeeName;
    private String employeeEmail;
    private Date employeeRetirementDate;
    private List<EmployeeRoleDTO> employeeRole;
    private Date employeeBirthDate;
    private Date joinDate;
    private String empAddress;
    private String empPhone;
    private DepartmentDTO department;
    private Collection<GrantedAuthority> authorities; //컬렉션을 반환하는데, 이는 사용자의 역할을 나타낸다.



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>(); //이 리스트는 나중에 반환될 사용자 권한의 컬렉션을 저장합니다.
        if(employeeRole != null) {
            employeeRole.forEach(role -> {
                authorities.add(() -> role.getAuthority().getAuthorityName());
            });
            return authorities;  //사용자 권한 리스트 반환
        }
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return this.employeePassword;
    }

    @Override
    public String getUsername() {
        return this.employeeId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
