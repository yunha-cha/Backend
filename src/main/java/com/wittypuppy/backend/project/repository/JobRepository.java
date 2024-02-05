package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.Employee;
import com.wittypuppy.backend.project.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Project_JobRepository")
public interface JobRepository extends JpaRepository<Job, Long> {
}
