package com.wittypuppy.backend.mypage.service;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import com.wittypuppy.backend.mypage.dto.MyPageEmpDTO;
import com.wittypuppy.backend.mypage.dto.MyPageUpdateDTO;
import com.wittypuppy.backend.mypage.entity.MyPageEmp;
import com.wittypuppy.backend.mypage.entity.MyPageUpdateEmp;
import com.wittypuppy.backend.mypage.repository.MyPageRepository;
import com.wittypuppy.backend.mypage.repository.MyPageUpdateRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MyPageService {

    private final MyPageRepository myPageRepository;
    private final ModelMapper modelMapper;
    private final MyPageUpdateRepository myPageUpdateRepository;

    public MyPageService(MyPageRepository myPageRepository, ModelMapper modelMapper, MyPageUpdateRepository myPageUpdateRepository) {
        this.myPageRepository = myPageRepository;
        this.modelMapper = modelMapper;
        this.myPageUpdateRepository = myPageUpdateRepository;
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
            myPageUpdateEmp.empPwd(newHashPwd);
            log.info("마이페이지 비밀번호 변경 중간의 끝");
            myPageUpdateRepository.save(myPageUpdateEmp);
            return modelMapper.map(myPageUpdateEmp, MyPageUpdateDTO.class);

        }else {
            throw new RuntimeException("현재 비밀번호가 잘못되었습니다. 비밀번호 변경에 실패했습니다. ");
        }


    }




}
