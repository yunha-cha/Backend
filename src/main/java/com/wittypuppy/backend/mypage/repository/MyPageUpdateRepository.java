package com.wittypuppy.backend.mypage.repository;

import com.wittypuppy.backend.mypage.entity.MyPageEmp;
import com.wittypuppy.backend.mypage.entity.MyPageUpdateEmp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyPageUpdateRepository extends JpaRepository<MyPageUpdateEmp, Long> {
}
