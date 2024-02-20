package com.wittypuppy.backend.group.repository;

import com.wittypuppy.backend.group.dto.GroupEmpDTO;
import com.wittypuppy.backend.group.entity.GroupDept;
import com.wittypuppy.backend.group.entity.GroupEmp;
import jakarta.validation.constraints.Null;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository("Group_Repository")
public interface GroupEmpRepository extends JpaRepository<GroupEmp, Long> {
//    Page<GroupEmp> findByRetirementDate(Object o, Pageable paging);

//조직도에서 사원정보리스트 가져오기
@Query("SELECT gr FROM GROUP_EMPLOYEE gr LEFT JOIN FETCH gr.department WHERE gr.retirementDate IS NULL")
Page<GroupEmp> findByRetirementDateWithDepartment(Pageable paging);

//    GroupEmp findByEmpNameOrDeptName(String employeeName, String departmentName);

    List<GroupEmp> findAllByEmpNameAndRetirementDateIsNullOrDepartment_DeptNameAndRetirementDateIsNull(String employeeName, String departmentName);

    List<GroupEmp> findByDepartment(GroupDept parentDepartment);


//    GroupEmp findByEmpNameOrDeptName(String employeeName, String departmentName);

//조직도에서 그룹명이랑 부서명으로 검색해서 그룹차트리스트 가져오기




}
