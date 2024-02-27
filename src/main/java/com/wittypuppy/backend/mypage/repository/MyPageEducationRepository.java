package com.wittypuppy.backend.mypage.repository;

import com.wittypuppy.backend.mypage.entity.MyPageEducation;
import com.wittypuppy.backend.mypage.entity.MyPageProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("mypage_education")
public interface MyPageEducationRepository extends JpaRepository<MyPageEducation, Long> {
}
