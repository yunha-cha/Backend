package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineRepository extends JpaRepository<ApprovalLine, Long> {
    Page<ApprovalLine> findByLineEmployeeCode_employeeCodeAndApprovalProcessStatus(Pageable paging, Long employeeCode, String 반려);



//    List<ApprovalLine> loginArrival();


//    List<ApprovalLine> inputArrival();
}
