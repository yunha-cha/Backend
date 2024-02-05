package com.wittypuppy.backend.approval.repository;

import com.wittypuppy.backend.approval.entity.ApprovalDoc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalDocRepository extends JpaRepository<ApprovalDoc, Long> {
}
