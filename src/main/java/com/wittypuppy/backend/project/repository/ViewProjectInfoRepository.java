package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.viewProjectInfo.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewProjectInfoRepository extends JpaRepository<Project,Long> {
}
