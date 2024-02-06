package com.wittypuppy.backend.mypage.repository;

import com.wittypuppy.backend.mypage.entity.MyPageEmp;
import com.wittypuppy.backend.mypage.entity.MyPageUpdateEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("MyPage_Update_Repository")
public interface MyPageUpdateRepository extends JpaRepository<MyPageUpdateEmp, Long> {
}
