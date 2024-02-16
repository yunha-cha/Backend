package com.wittypuppy.backend.group.service;


import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.group.dto.ChartData;
import com.wittypuppy.backend.group.dto.ChartState;
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


//    ==================== 여기부터 조직도 데이터 뽑아오기 ====================


//public List<ChartData> getChartData() {
//    List<GroupDept> departmentList = groupDeptRepository.findAll(); // 데이터베이스에서 모든 부서 정보 가져오기
//
//    Map<Long, ChartData> departmentMap = new HashMap<>();
//    List<ChartData> orgChartData = new ArrayList<>();
//
//    // Step 1: 최상위 본부 식별
//    List<GroupDept> rootDepartments = new ArrayList<>();
//    for (GroupDept department : departmentList) {
//        if (department.getParentDeptCode() == null) {
//            rootDepartments.add(department);
//        }
//    }
//
//    // Step 2: 각 본부의 하위 부서 및 사원 추가
//    for (GroupDept rootDepartment : rootDepartments) {
//        ChartData departmentNode = createDepartmentNode(rootDepartment);
//        groupChildren(rootDepartment, departmentNode, departmentList);
//        orgChartData.add(departmentNode);
//    }
//
//    return orgChartData;
//}
//    private String generateUniqueId(String prefix) {
//        // 접두사와 UUID를 결합하여 고유한 아이디 생성
//        return prefix + "-" + UUID.randomUUID().toString();
//    }
//
//    // 재귀적으로 하위 부서 및 사원 추가
//    private void groupChildren(GroupDept parentDepartment, ChartData parentNode, List<GroupDept> departmentList) {
//        List<ChartData> children = new ArrayList<>();
//        for (GroupDept department : departmentList) {
//            if (parentDepartment.getDeptCode().equals(department.getParentDeptCode())) {
//                ChartData departmentNode = createDepartmentNode(department);
//                groupChildren(department, departmentNode, departmentList);
//                children.add(departmentNode);
//            }
//        }
//
//        // 해당 부서의 사원을 추가
//        List<GroupEmp> employees = groupEmpRepository.findByDepartment(parentDepartment);
//        for (GroupEmp employee : employees) {
//            ChartData employeeNode = createEmployeeNode(employee);
//            children.add(employeeNode);
//        }
//
//    }
//
//    // 본부 또는 부서를 jstree와 호환되는 형태로 변환
//        private ChartData createDepartmentNode(GroupDept department) {
//        ChartData departmentNode = new ChartData();
//        departmentNode.setId(String.valueOf(department.getDeptCode()));
////        departmentNode.setId(generateUniqueId("dept"));//접두사 추가해서 아이디 중복방지
//        departmentNode.setParent(department.getParentDeptCode() == null ? "#" : String.valueOf(department.getParentDeptCode()));
//        departmentNode.setText(department.getDeptName());
//        departmentNode.setType("dept");
//        departmentNode.setState(new ChartState(true)); // 예시로 모든 부서가 열려있다고 가정
//        // 다른 필요한 정보 추가
//        return departmentNode;
//    }
//
//
//    // 사원을 jstree와 호환되는 형태로 변환
//    private ChartData createEmployeeNode(GroupEmp employee) {
//        ChartData employeeNode = new ChartData();
//        employeeNode.setId("emp_" + String.valueOf(employee.getEmpCode())); // 접두사를 붙여 고유한 아이디 생성
//        employeeNode.setParent(String.valueOf(employee.getDepartment().getDeptCode())); // 부모 노드의 아이디를 접두사와 함께 저장
//        employeeNode.setText(employee.getEmpName());
//        employeeNode.setType("employee");
//        // 다른 필요한 정보 추가
//        return employeeNode;
//    }

    public List<ChartData> getChartData() {
        List<GroupDept> departmentList = groupDeptRepository.findAll(); // 데이터베이스에서 모든 부서 정보 가져오기
        List<GroupEmp> employeeList = groupEmpRepository.findAll(); // 데이터베이스에서 모든 사원 정보 가져오기

        Map<Long, ChartData> nodeMap = new HashMap<>();
        List<ChartData> orgChartData = new ArrayList<>();

//        // 본부를 최상위로 추가
//        for (GroupDept department : departmentList) {
//            if (department.getParentDeptCode() == null) {
//                ChartData departmentNode = createDepartmentNode(department);
//                nodeMap.put(department.getDeptCode(), departmentNode);
//                orgChartData.add(departmentNode);
//                break; // 최상위 본부를 추가했으므로 더 이상 반복할 필요가 없음
//            }
//        }
//
//        // 부서를 추가
//        for (GroupDept department : departmentList) {
//            if (department.getParentDeptCode() != null) {
//                ChartData departmentNode = createDepartmentNode(department);
//                nodeMap.put(department.getDeptCode(), departmentNode);
//                // 부모 본부를 찾아서 설정
//                ChartData parent = nodeMap.get(department.getParentDeptCode());
//                if (parent != null) {
//                    departmentNode.setParent(parent.getId());
//                }
//                orgChartData.add(departmentNode);
//            }
//        }

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
                    nodeMap.put(department.getDeptCode(), departmentNode);
                    departmentNode.setParent(parent.getId());
                    orgChartData.add(departmentNode);
                }
            }
        }


        // 사원을 추가
        for (GroupEmp employee : employeeList) {
            ChartData employeeNode = createEmployeeNode(employee);
            // 부서의 아이디를 찾아서 부모로 설정
            ChartData parent = nodeMap.get(employee.getDepartment().getDeptCode());
            if (parent != null) {
                employeeNode.setParent(parent.getId());
            }
            orgChartData.add(employeeNode);
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
