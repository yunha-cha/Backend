package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceLineRepository extends JpaRepository<ApprovalLine, Long> {



    //내가 반려한 문서
    Page<ApprovalLine> findByLineEmployeeCode_employeeCodeAndApprovalProcessStatus(Pageable paging, Long employeeCode, String 반려);

}
