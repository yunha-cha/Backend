package com.wittypuppy.backend.group.repository;

import com.wittypuppy.backend.group.entity.GroupDept;
import com.wittypuppy.backend.group.entity.GroupJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Group_Job_Repository")
public interface GroupJobRepository extends JpaRepository<GroupJob, Long> {
}
