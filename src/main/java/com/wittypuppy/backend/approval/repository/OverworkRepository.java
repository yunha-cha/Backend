package com.wittypuppy.backend.approval.repository;

import com.wittypuppy.backend.approval.entity.OnLeave;
import com.wittypuppy.backend.approval.entity.Overwork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OverworkRepository extends JpaRepository<Overwork, Long> {
}
