package com.wittypuppy.backend.attendance.service;

import com.wittypuppy.backend.attendance.dto.AttendanceManagementDTO;
import com.wittypuppy.backend.attendance.dto.AttendanceWorkTypeDTO;
import com.wittypuppy.backend.attendance.entity.AttendanceManagement;
import com.wittypuppy.backend.attendance.entity.AttendanceWorkType;
import com.wittypuppy.backend.attendance.entity.Employee;
import com.wittypuppy.backend.attendance.paging.Criteria;
import com.wittypuppy.backend.attendance.repository.AttendanceRepository;
import com.wittypuppy.backend.attendance.repository.WorkTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final WorkTypeRepository workTypeRepository;

    private final ModelMapper modelMapper;

    public AttendanceService(AttendanceRepository attendanceRepository, WorkTypeRepository workTypeRepository, ModelMapper modelMapper) {
        this.attendanceRepository = attendanceRepository;
        this.workTypeRepository = workTypeRepository;
        this.modelMapper = modelMapper;
    }

    public Page<AttendanceManagementDTO> selectCommuteListWithPaging(Criteria cri) {

        System.out.println("=============selectCommuteList start= service===============");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("attendanceManagementCode").descending());

        Page<AttendanceManagement> result = attendanceRepository.findAll(paging);

        Page<AttendanceManagementDTO> attendanceWorkTypeList = result.map(attendance -> modelMapper.map(attendance, AttendanceManagementDTO.class));


        System.out.println("========== selectCommuteListWithPaging End ===========");

        return attendanceWorkTypeList;
    }

    public List<AttendanceWorkTypeDTO> workTypeCommute() {

        System.out.println("=============workTypeCommute start= service===============");

        List<AttendanceWorkType> workResult = workTypeRepository.typeAll();

        System.out.println("====== workResult ====== " + workResult);
        return workResult.stream().map(workType -> modelMapper.map(workType, AttendanceWorkTypeDTO.class)).collect(Collectors.toList());
    }
}
