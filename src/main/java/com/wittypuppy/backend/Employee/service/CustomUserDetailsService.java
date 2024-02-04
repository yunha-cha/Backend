package com.wittypuppy.backend.Employee.service;


import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.Employee.entity.LoginEmployeeRole;
import com.wittypuppy.backend.Employee.repository.EmployeeRepository;
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
    public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {

        LoginEmployee employee = employeeRepository.findByEmployeeId(employeeId);
        /* MemberDTO는 엔티티를 옮겨 담는 DTO이자 UserDetails이다. */
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);

        List<GrantedAuthority> authorities = new ArrayList<>();
        for(LoginEmployeeRole employeeRole : employee.getEmployeeRole()){
            String authorityName = employeeRole.getAuthority().getAuthorityName();
            authorities.add(new SimpleGrantedAuthority(authorityName));
        }

        employeeDTO.setAuthorities(authorities);

        return employeeDTO;
    }
}
