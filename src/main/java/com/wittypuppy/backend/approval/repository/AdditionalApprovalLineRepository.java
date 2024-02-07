package com.wittypuppy.backend.approval.repository;

import com.wittypuppy.backend.approval.entity.AdditionalApprovalLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdditionalApprovalLineRepository extends JpaRepository<AdditionalApprovalLine, Long> {

    List<AdditionalApprovalLine> findByApprovalDocCode(Long approvalDocCode);
}
