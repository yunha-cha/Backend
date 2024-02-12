package com.wittypuppy.backend.approval.repository;

import com.wittypuppy.backend.approval.entity.OnLeave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnLeaveRepository extends JpaRepository<OnLeave, Long> {
}
