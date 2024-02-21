package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentWorkType extends JpaRepository<WorkType, Long> {
    WorkType findByWorkTypeDocCode(Long approvalDocumentCode);
}
