package com.wittypuppy.backend.Employee.service;


import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.Employee.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public EmployeeDTO selectMyInfo(String employeeId) {
        log.info("[MemberService]  selectMyInfo   Start =============== ");

        LoginEmployee employee = employeeRepository.findByEmployeeId(employeeId);
        log.info("[employeeService]  {} =============== ", employee);
        log.info("[employeeService]  selectMyInfo   End =============== ");
        return modelMapper.map(employee, EmployeeDTO.class);
    }
}
