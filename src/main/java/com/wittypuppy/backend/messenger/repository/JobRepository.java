package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Employee;
import com.wittypuppy.backend.messenger.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
