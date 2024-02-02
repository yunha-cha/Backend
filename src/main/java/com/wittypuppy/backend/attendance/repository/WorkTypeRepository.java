package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.AttendanceWorkType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkTypeRepository extends  JpaRepository<AttendanceWorkType, Integer>{

    Page<AttendanceWorkType> findAll(Specification<AttendanceWorkType> spec, Pageable paging);
}
