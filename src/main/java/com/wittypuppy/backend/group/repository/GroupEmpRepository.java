package com.wittypuppy.backend.group.repository;

import com.wittypuppy.backend.group.entity.GroupEmp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupEmpRepository extends JpaRepository<GroupEmp, Long> {

    List<GroupEmp> findGroupEmpByEmpCode(Long empCode);



}
