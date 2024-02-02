package com.wittypuppy.backend.attendance.service;

import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<ApprovalLine, Long> {
    Page<ApprovalLine> findByLineEmployeeCode_employeeCodeAndApprovalProcessStatus(Pageable paging, Long employeeCode, String 반려);


}
