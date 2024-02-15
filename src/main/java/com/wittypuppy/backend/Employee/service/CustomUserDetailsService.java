package com.wittypuppy.backend.Employee.service;


import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.Employee.entity.LoginEmployeeRole;
import com.wittypuppy.backend.Employee.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    public CustomUserDetailsService(EmployeeRepository employeeRepository,
                                    ModelMapper modelMapper){
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("직원 아이디 출력 확인용"+ username);
        log.info("직원 아이디 출력용",username);

        System.out.println("employeeId = " + username);
        LoginEmployee employee = employeeRepository.findByEmployeeId(username);
        System.out.println("employeeId = " + employee);
        /* EmployeeDTO는 엔티티를 옮겨 담는 DTO이자 UserDetails이다. */
        User user = modelMapper.map(employee, User.class);

        List<GrantedAuthority> authorities = new ArrayList<>();
        for(LoginEmployeeRole employeeRole : employee.getEmployeeRole()){
            String authorityName = employeeRole.getAuthority().getAuthorityName();
            authorities.add(new SimpleGrantedAuthority(authorityName));
        }

        user.setAuthorities(authorities);

        return user;
    }
}
