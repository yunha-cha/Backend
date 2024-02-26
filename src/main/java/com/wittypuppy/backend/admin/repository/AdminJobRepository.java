package com.wittypuppy.backend.admin.repository;

import com.wittypuppy.backend.admin.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminJobRepository extends JpaRepository<Job, Long> {
    Job findByJobName(String jobName);
}
