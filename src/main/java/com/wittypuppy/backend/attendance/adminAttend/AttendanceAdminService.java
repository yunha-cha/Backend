package com.wittypuppy.backend.attendance.adminAttend;

import com.wittypuppy.backend.attendance.dto.EmployeeDTO;
import com.wittypuppy.backend.attendance.entity.Employee;
import com.wittypuppy.backend.attendance.paging.Criteria;
import com.wittypuppy.backend.attendance.repository.AttendanceAdminEmployee;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttendanceAdminService {


    private final AttendanceAdminEmployee attendanceAdminEmployee;
    private final ModelMapper modelMapper;

    public AttendanceAdminService(AttendanceAdminEmployee attendanceAdminEmployee, ModelMapper modelMapper) {
        this.attendanceAdminEmployee = attendanceAdminEmployee;
        this.modelMapper = modelMapper;
    }

    public Page<AdminEmployeeDTO> mainVacation(Criteria cri) {

        System.out.println("=====service=====mainVacation tStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.unsorted());


        Page<AdminEmployee> result = attendanceAdminEmployee.findEmp(paging);

        Page<AdminEmployeeDTO> resultList = result.map(adminVacation -> modelMapper.map(adminVacation, AdminEmployeeDTO.class));


        System.out.println("========resultList======= " + resultList);
        System.out.println("======== mainVacation end ============");

        return resultList;

    }

    public Page<AdminEmployeeDTO> noVacation(Criteria cri) {

        System.out.println("=====service=====noVacation tStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.unsorted());


        Page<AdminEmployee> result = attendanceAdminEmployee.findNo(paging);

        Page<AdminEmployeeDTO> resultList = result.map(adminNoVacation -> modelMapper.map(adminNoVacation, AdminEmployeeDTO.class));


        System.out.println("========resultList======= " + resultList);
        System.out.println("======== noVacation end ============");

        return resultList;

    }
}
