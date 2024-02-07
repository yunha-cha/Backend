package com.wittypuppy.backend.mypage.service;

import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import com.wittypuppy.backend.mypage.dto.MyPageEmpDTO;
import com.wittypuppy.backend.mypage.dto.MyPageProfileDTO;
import com.wittypuppy.backend.mypage.dto.MyPageUpdateDTO;
import com.wittypuppy.backend.mypage.entity.MyPageEmp;
import com.wittypuppy.backend.mypage.entity.MyPageProfile;
import com.wittypuppy.backend.mypage.entity.MyPageUpdateEmp;
import com.wittypuppy.backend.mypage.repository.MyPageProfileRepository;
import com.wittypuppy.backend.mypage.repository.MyPageRepository;
import com.wittypuppy.backend.mypage.repository.MyPageUpdateRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MyPageService {

    private final MyPageRepository myPageRepository;
    private final ModelMapper modelMapper;
    private final MyPageUpdateRepository myPageUpdateRepository;

    @Autowired
    private final MyPageProfileRepository myPageProfileRepository;

    private final String imageDirectory = "classpath:/static/web-images/";


    public MyPageService(MyPageRepository myPageRepository, ModelMapper modelMapper, MyPageUpdateRepository myPageUpdateRepository, MyPageProfileRepository myPageProfileRepository) {
        this.myPageRepository = myPageRepository;
        this.modelMapper = modelMapper;
        this.myPageUpdateRepository = myPageUpdateRepository;
        this.myPageProfileRepository = myPageProfileRepository;
    }


    public MyPageEmpDTO selectEmpByEmpCode(Long empCode){
        log.info("마이페이지 서비스 시작___-----=====");

        MyPageEmp myPageEmp = myPageRepository.findById(empCode).get();//findById는 스프링부트에서 원래 있는 문법으로 empcode라는 pk값으로 값을 불러온다.

        MyPageEmpDTO myPageEmpDTO = modelMapper.map(myPageEmp, MyPageEmpDTO.class);
        return myPageEmpDTO;

    }

    @Transactional
    public MyPageUpdateEmp updateMyPageByEmpCode(MyPageUpdateDTO myPageUpdateDTO, Long empCode){
        log.info("마이페이지 내정보 수정하기 시작");
       try {
           MyPageUpdateEmp myPageUpdateEmp = myPageUpdateRepository.findById(empCode).orElseThrow(() -> new DataNotFoundException("해당 마이페이지 내정보 데이터를 찾을 수 없습니다."));
           //이거 빌더 패턴으로 써줘야됨 엔티티에 새터하고 디티오명이랑 마지막에 빌더 수정하고싶은거 여러가지 쓰면됨
           myPageUpdateEmp.Phone(myPageUpdateDTO.getPhone())
                   .empEmail(myPageUpdateDTO.getEmpEmail())
                   .address(myPageUpdateDTO.getAddress());
           log.info("Mypaservice >>> mypageupdateemp>>>> end");

           return myPageUpdateEmp;
       }catch (Exception e){
           log.error("MyPageService 에서 myPageUpdateEmp로 가는중에 에러");
           throw new DataUpdateException("MypageUpdate 데이터 갱신 중 에러 발생");
       }

    }


    @Transactional
    public MyPageUpdateDTO updateEmpPwdByEmpCode(Long empCode , String empPwd, String newEmpPwd){
        log.info("마이페이지 서비스 비밀번호 변경 시작");
        MyPageUpdateEmp myPageUpdateEmp = myPageUpdateRepository.findById(empCode).orElseThrow(() -> new DataNotFoundException("해당 마이페이지 비밀번호 변경 데이터를 찾을 수 없습니다."));

        System.out.println("myPageUpdateEmp 나오냐 = " + myPageUpdateEmp);
        System.out.println("myPageUpdateEmp 비번 나오냐 = " + myPageUpdateEmp.getEmpPwd());
        log.info("사원 비밀번호 출력 확인",myPageUpdateEmp.getEmpPwd());
        if(myPageUpdateEmp != null && BCrypt.checkpw(empPwd, myPageUpdateEmp.getEmpPwd())){

            log.info("마이페이지 비밀번호 변경 중간");
            //현재 비밀번호랑 일치하면 새로운 비밀번호로 업데이트
            String newHashPwd = BCrypt.hashpw(newEmpPwd, BCrypt.gensalt());
            myPageUpdateEmp.setEmpPwd(newHashPwd);
            log.info("마이페이지 비밀번호 변경 중간의 끝");
            myPageUpdateRepository.save(myPageUpdateEmp);
            return modelMapper.map(myPageUpdateEmp, MyPageUpdateDTO.class);

        }else {
            throw new RuntimeException("현재 비밀번호가 잘못되었습니다. 비밀번호 변경에 실패했습니다. ");
        }
    }



//    public MyPageProfileDTO updateProfile(Long employeeCode, MultipartFile file) {
//        MyPageEmp myPageEmp = new MyPageEmp();
//        myPageEmp.setEmpCode(employeeCode);
//
//        // 프로필 사진 업데이트 로직 구현
//        myPageProfileRepository.updateProfileDeleteStatusByEmployeeCode(employeeCode);
//
//        List<MyPageProfile> profilesToUpdate = myPageProfileRepository.findByEmployee_EmpCodeAndProfileDeleteStatus(employeeCode, "y");
//
//        MyPageProfileDTO fileSavePath = saveProfileImage(file);
//
//        for (MyPageProfile profile : profilesToUpdate) {
//            profile.setProfileOgFile(String.valueOf(fileSavePath));
//            profile.setProfileChangedFile(String.valueOf(fileSavePath));
//            profile.setProfileRegistDate(LocalDateTime.now());
//            profile.setProfileDeleteStatus("N");
//        }
//
//        myPageProfileRepository.saveAll(profilesToUpdate);
//
////        return imageDirectory + fileSavePath; // 완전한 이미지 URL 반환
//        return fileSavePath;
//    }
//
//    private MyPageProfileDTO saveProfileImage(MultipartFile file) {
//        try {
//            // 실제 파일 저장 로직
//            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            Path filePath = Path.of(imageDirectory, fileName);
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
////            return fileName;
//            return new MyPageProfileDTO(fileName); // MyPageProfileDTO 객체 생성 후 반환
//
//        } catch (IOException e) {
//            throw new RuntimeException("파일 변경 실", e);
//        }
//    }
}
