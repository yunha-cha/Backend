package com.wittypuppy.backend.group.repository;

import com.wittypuppy.backend.group.entity.GroupDept;
import com.wittypuppy.backend.group.entity.GroupEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Group_Dept_Repository")
public interface GroupDeptRepository extends JpaRepository<GroupDept, Long> {

}
