package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.ProjectAndMemberAndPostAndPostMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectAndMemberAndPostAndPostMemberRepository
        extends JpaRepository<ProjectAndMemberAndPostAndPostMember, Long> {

}
