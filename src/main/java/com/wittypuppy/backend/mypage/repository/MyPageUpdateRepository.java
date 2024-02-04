package com.wittypuppy.backend.mypage.repository;

import com.wittypuppy.backend.mypage.entity.MyPageEmp;
import com.wittypuppy.backend.mypage.entity.MyPageUpdateEmp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPageUpdateRepository extends JpaRepository<MyPageUpdateEmp, Long> {
}
