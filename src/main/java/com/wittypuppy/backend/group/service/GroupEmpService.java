package com.wittypuppy.backend.group.service;


import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.group.dto.ChartData;
import com.wittypuppy.backend.group.dto.ChartState;
import com.wittypuppy.backend.group.dto.GroupDeptDTO;
import com.wittypuppy.backend.group.dto.GroupEmpDTO;
import com.wittypuppy.backend.group.entity.GroupDept;
import com.wittypuppy.backend.group.entity.GroupEmp;
import com.wittypuppy.backend.group.repository.GroupDeptRepository;
import com.wittypuppy.backend.group.repository.GroupEmpRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GroupEmpService {

    private final GroupEmpRepository groupEmpRepository;
    private final GroupDeptRepository groupDeptRepository;

    private final ModelMapper modelMapper;

    public GroupEmpService(GroupEmpRepository groupEmpRepository, GroupDeptRepository groupDeptRepository, ModelMapper modelMapper) {
        this.groupEmpRepository = groupEmpRepository;
        this.groupDeptRepository = groupDeptRepository;
        this.modelMapper = modelMapper;
    }

    /* 조직도에서 retired상태가 null인 사람 찾기 */

    public Page<GroupEmpDTO> selectEmpListWithGroupPaging(Criteria criteria){
//        log.info("empservice 시작");
        int index = criteria.getPageNum() - 1;
        int count = criteria.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("empCode").ascending()); //오름차순

        Page<GroupEmp> result = groupEmpRepository.findByRetirementDateWithDepartment(paging);

        Page<GroupEmpDTO> groupList = result.map(group -> modelMapper.map(group, GroupEmpDTO.class));

        log.info("조직에서 그룹 리스트 뽑아내기", groupList);
        System.out.println("groupList 나오냐 = " + groupList);
        return groupList;
    }

    public List<GroupEmpDTO> selectGroupList(String employeeName, String departmentName) {
        log.info("부서명이랑 사원명으로 조회하는 서비스 시작");

        List<GroupEmpDTO> groupEmpDTOList = groupEmpRepository.findAllByEmpNameAndRetirementDateIsNullOrDepartment_DeptNameAndRetirementDateIsNull(employeeName, departmentName)//여기서 All을 써줘야지 list를 받아올수 있어서 find와 By사이에 all을 써줌
                .stream()
                .map(groupEmp -> modelMapper.map(groupEmp, GroupEmpDTO.class))
                .collect(Collectors.toList());

        log.info("부서명이랑 사원명으로 조회하는 서비스 종료");
        return groupEmpDTOList;
    }


    @Transactional
    public void addDepartment(String deptName, Long parentDeptCode) {
        GroupDept department = new GroupDept();
        department.setDeptName(deptName);
        department.setParentDeptCode(parentDeptCode);
        groupDeptRepository.save(department);
        System.out.println("신규 부서가 추가되었습니다.");
    }



//    ==================== 여기부터 조직도 데이터 뽑아오기 ====================

    public List<ChartData> getChartData() {
        List<GroupDept> departmentList = groupDeptRepository.findAll(); // 데이터베이스에서 모든 부서 정보 가져오기
        List<GroupEmp> employeeList = groupEmpRepository.findAll(); // 데이터베이스에서 모든 사원 정보 가져오기

        Map<Long, ChartData> nodeMap = new HashMap<>();
        List<ChartData> orgChartData = new ArrayList<>();

        // 부서를 처리
        for (GroupDept department : departmentList) {
            if (department.getParentDeptCode() == null) {
                // 최상위 부서인 경우
                ChartData departmentNode = createDepartmentNode(department);
                nodeMap.put(department.getDeptCode(), departmentNode);
                orgChartData.add(departmentNode);
            } else {
                // 최상위 부서가 아닌 경우, 부모 부서를 찾아서 추가
                ChartData parent = nodeMap.get(department.getParentDeptCode());
                if (parent != null) {
                    ChartData departmentNode = createDepartmentNode(department);
                    departmentNode.setState(new ChartState(false));
                    nodeMap.put(department.getDeptCode(), departmentNode);
                    departmentNode.setParent(parent.getId());
                    orgChartData.add(departmentNode);
                }
            }
        }


        // 사원을 추가
        for (GroupEmp employee : employeeList) {
            if (employee.getRetirementDate() == null) { // 퇴사일이 null인 경우에만 추가
                ChartData employeeNode = createEmployeeNode(employee);
                employeeNode.setState(new ChartState(false));
                // 부서의 아이디를 찾아서 부모로 설정
                ChartData parent = nodeMap.get(employee.getDepartment().getDeptCode());
                if (parent != null) {
                    employeeNode.setParent(parent.getId());
                }
                orgChartData.add(employeeNode);
            }
        }

        return orgChartData;
    }

    // 본부를 jstree와 호환되는 형태로 변환
    private ChartData createDepartmentNode(GroupDept department) {
        ChartData departmentNode = new ChartData();
        departmentNode.setId("dept_" + String.valueOf(department.getDeptCode()));
        departmentNode.setParent("#"); // 본부는 최상위 노드이므로 부모를 "#"으로 설정
        departmentNode.setText(department.getDeptName());
        departmentNode.setType("dept");
        departmentNode.setState(new ChartState(true)); // 예시로 모든 부서가 열려있다고 가정
        // 다른 필요한 정보 추가
        return departmentNode;
    }


    // 사원을 jstree와 호환되는 형태로 변환
    private ChartData createEmployeeNode(GroupEmp employee) {
        ChartData employeeNode = new ChartData();
        employeeNode.setId("emp_" + String.valueOf(employee.getEmpCode()));
        employeeNode.setParent("dept_" + String.valueOf(employee.getDepartment().getDeptCode()));
        employeeNode.setText(employee.getEmpName());
        employeeNode.setType("employee");
        // 다른 필요한 정보 추가
        return employeeNode;
    }


}
