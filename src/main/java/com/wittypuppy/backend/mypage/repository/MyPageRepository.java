package com.wittypuppy.backend.mypage.repository;

import com.wittypuppy.backend.mypage.entity.MyPageEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("MyPage_Repository")
public interface MyPageRepository extends JpaRepository<MyPageEmp, Long> {

}
