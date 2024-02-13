package com.wittypuppy.backend.approval.repository;

import com.wittypuppy.backend.approval.entity.ApprovalRepresent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepresentRepository extends JpaRepository<ApprovalRepresent, Long> {

    ApprovalRepresent findByRepresentative(Long employeeCode);
}
