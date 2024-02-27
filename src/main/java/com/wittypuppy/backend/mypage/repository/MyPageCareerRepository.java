package com.wittypuppy.backend.mypage.repository;

import com.wittypuppy.backend.mypage.entity.MyPageCareer;
import com.wittypuppy.backend.mypage.entity.MyPageProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("mypage_career")
public interface MyPageCareerRepository extends JpaRepository<MyPageCareer, Long> {
}
