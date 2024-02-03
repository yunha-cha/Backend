package com.wittypuppy.backend.mypage.service;

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

//    @Transactional
//    public String updateDemo(DemoDTO demoDTO, Long demoCode) {
//        log.info("DemoService >>> updateDemo >>> start");
//
//        try {
//            Demo demo = demoRepository.findById(demoCode)
//                    .orElseThrow(() -> new DataNotFoundException("해당 Demo 데이터를 찾을 수 없습니다."));
//            demo.column1(demoDTO.getColumn1())
//                    .column2(demoDTO.getColumn2())
//                    .column3(demoDTO.getColumn3())
//                    .column4(demoDTO.getColumn4());
//            log.info("DemoService >>> updateDemo >>> end");
//            return "상품 수정 성공";
//        } catch (Exception e) {
//            log.error("DemoService >>> updateDemo >>> Error >>>", e);
//            throw new DataUpdateException("Demo 데이터 갱신 중 에러 발생");
//        }
//    }

    @Transactional
    public MyPageUpdateEmp updateMyPageByEmpCode(MyPageUpdateDTO myPageUpdateDTO, Long empCode){
        log.info("마이페이지 내정보 수정하기 시작");
       try {
           MyPageUpdateEmp myPageUpdateEmp = myPageUpdateRepository.findById(empCode).orElseThrow(() -> new DataNotFoundException("해당 마이페이지 내정보 데이터를 찾을 수 없습니다."));
           //이거 빌더 패턴으로 써줘야됨 엔티티에 새터하고 디티오명이랑 마지막에 빌더 수정하고싶은거 여러가지 쓰면됨
           myPageUpdateEmp.Phone(myPageUpdateDTO.getPhone())
                   .EmpEmail(myPageUpdateDTO.getEmpEmail())
                   .Address(myPageUpdateDTO.getAddress());
           log.info("Mypaservice >>> mypageupdateemp>>>> end");

           return myPageUpdateEmp;
       }catch (Exception e){
           log.error("MyPageService 에서 myPageUpdateEmp로 가는중에 에러");
           throw new DataUpdateException("MypageUpdate 데이터 갱신 중 에러 발생");
       }

    }

}
