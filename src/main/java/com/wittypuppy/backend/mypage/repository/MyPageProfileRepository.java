package com.wittypuppy.backend.mypage.repository;

import com.wittypuppy.backend.messenger.entity.ChatroomProfile;
import com.wittypuppy.backend.mypage.dto.MyPageProfileDTO;
import com.wittypuppy.backend.mypage.entity.MyPageProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository("MyPage_Profile_Repository")
public interface MyPageProfileRepository extends JpaRepository<MyPageProfile, Long> {
    Optional<MyPageProfile> findFirstByEmpCodeOrderByProfileRegistDateDesc(Long empCode);



//    Optional<ChatroomProfile> findFirstByEmployeeEmpCodeOrderByProfileRegistDateDesc(Long employeeCode);
//    List<MyPageProfile> findByEmployee_EmpCodeAndProfileDeleteStatus(Long empCode, String profileDeleteStatus);
//
//    @Transactional
//    @Modifying
//    @Query("UPDATE MYPAGE_PROFILE SET profileDeleteStatus = CASE WHEN employee.empCode = ?1 THEN 'N' ELSE 'Y' END WHERE employee.empCode = ?1")
//    void updateProfileDeleteStatusByEmployeeCode(Long empCode);
}