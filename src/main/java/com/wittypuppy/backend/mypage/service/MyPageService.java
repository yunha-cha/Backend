package com.wittypuppy.backend.mypage.service;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import com.wittypuppy.backend.mypage.dto.MyPageCareerDTO;
import com.wittypuppy.backend.mypage.dto.MyPageEducationDTO;
import com.wittypuppy.backend.mypage.dto.MyPageEmpDTO;
import com.wittypuppy.backend.mypage.dto.MyPageUpdateDTO;
import com.wittypuppy.backend.mypage.entity.*;
import com.wittypuppy.backend.mypage.repository.*;
import com.wittypuppy.backend.util.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class MyPageService {

    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    private final MyPageRepository myPageRepository;
    private final ModelMapper modelMapper;
    private final MyPageUpdateRepository myPageUpdateRepository;

    private final MyPageProfileRepository myPageProfileRepository;

    private final MyPageCareerRepository myPageCareerRepository;
    private final MyPageEducationRepository myPageEducationRepository;

    private final String imageDirectory = "classpath:/static/web-images/";

    @Autowired
    private PasswordEncoder passwordEncoder;


    public MyPageService(MyPageRepository myPageRepository, ModelMapper modelMapper, MyPageUpdateRepository myPageUpdateRepository, MyPageProfileRepository myPageProfileRepository, MyPageCareerRepository myPageCareerRepository, MyPageEducationRepository myPageEducationRepository) {
        this.myPageRepository = myPageRepository;
        this.modelMapper = modelMapper;
        this.myPageUpdateRepository = myPageUpdateRepository;
        this.myPageProfileRepository = myPageProfileRepository;
        this.myPageCareerRepository = myPageCareerRepository;
        this.myPageEducationRepository = myPageEducationRepository;
    }


    public MyPageEmpDTO selectEmpByEmpCode(Long empCode){
        log.info("마이페이지 서비스 시작___-----=====");

        MyPageEmp myPageEmp = myPageRepository.findById(empCode).get();//findById는 스프링부트에서 원래 있는 문법으로 empcode라는 pk값으로 값을 불러온다.
//        MyPageCareer myPageCareer = myPageCareerRepository.findById(empCode).get();
//        MyPageEducation myPageEducation = myPageEducationRepository.findById(empCode).get();

        MyPageEmpDTO myPageEmpDTO = modelMapper.map(myPageEmp, MyPageEmpDTO.class);

//        MyPageCareerDTO myPageCareerDTO = modelMapper.map(myPageCareer, MyPageCareerDTO.class);
//        MyPageEducationDTO myPageEducationDTO = modelMapper.map(myPageEducation, MyPageEducationDTO.class);
//
//
////        myPageEmpDTO.setCareer(myPageCareerDTO);
////       myPageEmpDTO.setEducation(myPageEducationDTO );


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
        System.out.println("myPageUpdateEmp.getEmpPwd() 사원 비밀번호 = " + myPageUpdateEmp.getEmpPwd());
        if(myPageUpdateEmp != null && passwordEncoder.matches(empPwd, myPageUpdateEmp.getEmpPwd())){

            log.info("마이페이지 비밀번호 변경 중간");
            //현재 비밀번호랑 일치하면 새로운 비밀번호로 업데이트
            String newHashPwd = passwordEncoder.encode(newEmpPwd);
            myPageUpdateEmp.setEmpPwd(newHashPwd);
            log.info("마이페이지 비밀번호 변경 중간의 끝");
            myPageUpdateRepository.save(myPageUpdateEmp);
            return modelMapper.map(myPageUpdateEmp, MyPageUpdateDTO.class);

        }else {
            System.out.println( "비밀번호가 잘못되었습니다.");
            throw new RuntimeException("현재 비밀번호가 잘못되었습니다");
        }
    }

//    @Transactional
//    public MyPageUpdateDTO updateEmpPwdByEmpCode(Long empCode , String empPwd, String newEmpPwd){
//        log.info("마이페이지 서비스 비밀번호 변경 시작");
//        MyPageUpdateEmp myPageUpdateEmp = myPageUpdateRepository.findById(empCode).orElseThrow(() -> new DataNotFoundException("해당 마이페이지 비밀번호 변경 데이터를 찾을 수 없습니다."));
//
//        System.out.println("myPageUpdateEmp 나오냐 = " + myPageUpdateEmp);
//        System.out.println("myPageUpdateEmp 비번 나오냐 = " + myPageUpdateEmp.getEmpPwd());
//        log.info("사원 비밀번호 출력 확인",myPageUpdateEmp.getEmpPwd());
//        System.out.println("myPageUpdateEmp.getEmpPwd() 사원 비밀번호 = " + myPageUpdateEmp.getEmpPwd());
//        if(myPageUpdateEmp != null && BCrypt.checkpw(empPwd, myPageUpdateEmp.getEmpPwd())){
//
//            log.info("마이페이지 비밀번호 변경 중간");
//            //현재 비밀번호랑 일치하면 새로운 비밀번호로 업데이트
//            String newHashPwd = BCrypt.hashpw(newEmpPwd, BCrypt.gensalt());
//            myPageUpdateEmp.setEmpPwd(newHashPwd);
//            log.info("마이페이지 비밀번호 변경 중간의 끝");
//            myPageUpdateRepository.save(myPageUpdateEmp);
//            return modelMapper.map(myPageUpdateEmp, MyPageUpdateDTO.class);
//
//        }else {
//            throw new RuntimeException("현재 비밀번호가 잘못되었습니다");
//        }
//    }


    public MyPageProfile findMyPageProfileImage(Long empCode) {
        Optional<MyPageProfile> myPageProfileOptional = myPageProfileRepository.findFirstByEmpCodeOrderByProfileRegistDateDesc(empCode);

        // 프로필을 찾지 못한 경우 예외 처리
        MyPageProfile myPageProfile = myPageProfileOptional.orElse(new MyPageProfile());

        System.out.println("myPageProfile 나오는지 홗인용 ============= = " + myPageProfile);
        System.out.println("myPageProfile.getProfileChangedFile() = " + myPageProfile.getProfileChangedFile());
        // 프로필 파일을 반환
        return myPageProfile;
    }

//    @Transactional
//    public String updateMyPageProfileImage(MultipartFile ProfileImage, Long empCode, @AuthenticationPrincipal User principal) {
//        try {
//            LocalDateTime now = LocalDateTime.now();
//            empCode = (long) principal.getEmployeeCode();
//
//            // 새로운 프로필을 생성합니다.
//            String imageName = UUID.randomUUID().toString().replace("-", "");
//            String replaceFileName = null;
//            try {
//                replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, ProfileImage);
//
//                // 새로운 프로필 정보를 생성합니다.
//                MyPageProfile newMyPageProfile = new MyPageProfile()
//                        .setEmpCode(empCode)
//                        .setProfileOgFile(ProfileImage.getOriginalFilename())
//                        .setProfileChangedFile(replaceFileName)
//                        .setProfileRegistDate(now)
//                        .setProfileDeleteStatus("N"); // 새 프로필을 등록할 때 삭제 상태를 'N'으로 설정합니다.
//
//                // 새 프로필 정보를 데이터베이스에 저장합니다.
//                myPageProfileRepository.save(newMyPageProfile);
//
//                // 이전 프로필이 존재하는 경우, 삭제 상태를 변경합니다.
//                MyPageProfile existingProfile = myPageProfileRepository.findFirstByEmpCodeOrderByProfileRegistDateDesc(empCode).orElse(null);
//                if (existingProfile != null) {
//                    existingProfile.setProfileDeleteStatus("Y");
//                    myPageProfileRepository.save(existingProfile);
//                }
//
//            } catch (IOException e) {
//                // 파일 저장 실패 시 예외 처리
//                if (replaceFileName != null) {
//                    FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
//                }
//                throw new DataUpdateException("프로필 사진 변경 실패");
//            }
//        } catch (Exception e) {
//            throw new DataUpdateException("프로필 사진 변경 실패");
//        }
//        return "프로필 사진 변경 성공";
//    }


    @Transactional
    public String updateMyPageProfileImage(MultipartFile profileImage, Long empCode, @AuthenticationPrincipal User principal) {
        try {
            LocalDateTime now = LocalDateTime.now();
            empCode = (long) principal.getEmployeeCode();

            // 기존 프로필을 조회합니다.
            Optional<MyPageProfile> existingProfileOptional = myPageProfileRepository.findFirstByEmpCodeOrderByProfileRegistDateDesc(empCode);
            if (existingProfileOptional.isPresent()) {
                // 기존 프로필이 존재하는 경우 삭제 상태를 "Y"로 설정합니다.
                MyPageProfile existingProfile = existingProfileOptional.get();
                existingProfile.setProfileDeleteStatus("Y");
                myPageProfileRepository.save(existingProfile);
            }
            System.out.println("ProfileImage 위에서 나오는지 = " + profileImage);

            // 새로운 프로필을 생성합니다.
            String imageName = UUID.randomUUID().toString().replace("-", "");
            String replaceFileName = null;
            try {
                replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, profileImage);

                System.out.println("ProfileImage 여기서 나오는지 = " + profileImage);
                // 새로운 프로필 정보를 생성합니다.
                MyPageProfile newMyPageProfile = new MyPageProfile()
                        .setEmpCode(empCode)
                        .setProfileOgFile(profileImage.getOriginalFilename())
                        .setProfileChangedFile(replaceFileName)
                        .setProfileRegistDate(now)
                        .setProfileDeleteStatus("N"); // 새 프로필을 등록할 때 삭제 상태를 'N'으로 설정합니다.

                // 새 프로필 정보를 데이터베이스에 저장합니다.
                myPageProfileRepository.save(newMyPageProfile);

            } catch (IOException e) {
                // 파일 저장 실패 시 예외 처리
                if (replaceFileName != null) {
                    FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
                }
                throw new DataUpdateException("프로필 사진 변경 실패");
            }
        } catch (Exception e) {
            throw new DataUpdateException("프로필 사진 변경 실패");
        }
        return "프로필 사진 변경 성공";
    }




}
