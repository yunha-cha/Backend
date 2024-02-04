package com.wittypuppy.backend.group.service;


import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.group.dto.GroupDeptDTO;
import com.wittypuppy.backend.group.dto.GroupEmpDTO;
import com.wittypuppy.backend.group.entity.GroupEmp;
import com.wittypuppy.backend.group.repository.GroupEmpRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GroupEmpService {

    private final GroupEmpRepository groupEmpRepository;

    private final ModelMapper modelMapper;

    public GroupEmpService(GroupEmpRepository groupEmpRepository, ModelMapper modelMapper) {
        this.groupEmpRepository = groupEmpRepository;
        this.modelMapper = modelMapper;
    }

    /* 조직도에서 retired상태가 null인 사람 찾기 */

    public Page<GroupEmpDTO> selectEmpListWithGroupPaging(Criteria criteria){
        log.info("empservice 시작");
        int index = criteria.getPageNum() - 1;
        int count = criteria.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("empCode").descending());

        Page<GroupEmp> result = groupEmpRepository.findByRetirementDateWithDepartment(paging);

        Page<GroupEmpDTO> groupList = result.map(group -> modelMapper.map(group, GroupEmpDTO.class));

        log.info("조직에서 그룹 리스트 뽑아내기", groupList);
        System.out.println("groupList 나오냐 = " + groupList);
        return groupList;
    }

    public List<GroupEmpDTO> selectGroupList(String employeeName, String departmentName) {
        log.info("부서명이랑 사원명으로 조회하는 서비스 시작");

        List<GroupEmpDTO> groupEmpDTOList = groupEmpRepository.findAllByEmpNameOrDepartment_DeptName(employeeName, departmentName)//여기서 All을 써줘야지 list를 받아올수 있어서 find와 By사이에 all을 써줌
                .stream()
                .map(groupEmp -> modelMapper.map(groupEmp, GroupEmpDTO.class))
                .collect(Collectors.toList());

        log.info("부서명이랑 사원명으로 조회하는 서비스 종료");
        return groupEmpDTOList;
    }





}
