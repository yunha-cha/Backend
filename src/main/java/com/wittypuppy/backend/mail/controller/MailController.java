package com.wittypuppy.backend.mail.controller;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.mail.dto.EmailDTO;
import com.wittypuppy.backend.mail.dto.EmployeeDTO;
import com.wittypuppy.backend.mail.service.EmailService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/mail")
@RestController
public class MailController {
    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    //예약한 메일 보내는 기능

    /**
     * 이메일 전송 메소드
     * @param email 사용자가 메일 쓰기 중 입력한 데이터
     * @param status send, temporary, reserve 로 나뉜다.(일반, 임시저장, 예약)
     * @return 응답
     */
    @PostMapping("send-mail")
    public ResponseEntity<ResponseDTO> sendMail(@RequestBody EmailDTO email,@RequestParam String status) {
        //에러 처리 하셈
        String receiverId = getId(email.getEmailReceiver().getEmployeeId());   //이메일 가져와서 아이디로 변환
        if(receiverId.equals("에러")) return resNull(1000,"이메일이 이상합니다.");

        email.setEmailReceiver(emailService.findByEmployeeCode(receiverId));    //가져갈 객체에 받는 사람 저장
        email.setEmailSender(new EmployeeDTO(1L));                 //가져갈 객체에 보내는 사람 저장
        email.setEmailSendTime(LocalDateTime.now());                            //보낼 시간 현재로 저장
        email.setEmailReadStatus("N");                                          //읽지 않음으로 저장

        email.setEmailStatus("send");                                            //이메일 상태 기본으로

        EmailDTO result = emailService.sendMail(email,status);

        return res("메일 보내기에 성공하였습니다.", result);
    }

    @GetMapping("/find-receive-mail")
    public ResponseEntity<ResponseDTO> findReceiveMail(@RequestParam String condition){

        List<EmailDTO> emailList = emailService.findReceiveMail(condition);
        if(emailList == null){
            return resNull(1003,"받은 이메일이 없습니다.");
        }
        return res("받은 이메일 조회 성공",emailList);
    }
    @GetMapping("/find-send-mail")
    public ResponseEntity<ResponseDTO> findSendMail(@RequestParam String condition){
        List<EmailDTO> emailList = emailService.findSendMail(condition);
        if(emailList == null){
            return resNull(1004,"보낸 이메일이 없습니다.");
        }
        return res("보낸 이메일 조회 성공",emailList);
    }

    /**
     * 이메일 상태 변경 메소드
     * @param emailDTOs 클라에서 가져온 이메일 코드들
     * @param status 클라에서 온 변경할 상태
     * @return 응답
     */
    @PutMapping("/update-mail-status")
    public ResponseEntity<ResponseDTO> updateMailStatus(@RequestBody List<EmailDTO> emailDTOs, @RequestParam String status){
        //프론트에 키가 있으니 그걸 primary 키로 가져오자
        try {
            List<EmailDTO> emails = emailService.findByAllEmailCode(emailDTOs);

            System.out.println("DTO로 잘 가져왔니?");
            for (EmailDTO email : emails) {
                System.out.println(email);
                email.setEmailStatus(status);
            }
            try {
                emails = emailService.updateEmailStatus(emails);
            } catch (TransactionSystemException e) {
                System.out.println("트랜잭션 에러");
                return resNull(1007, "트랜젝션 중 에러가 발생했습니다.");
            }
            return res("이메일이 " + status + " 이(가) 되었습니다.", emails);

        } catch (EmptyResultDataAccessException e){
            System.out.println("체크한 이메일을 찾을 수 없습니다.");
            return resNull(1006,"이메일을 찾을 수 없습니다.");
        } catch (Exception e){
            System.out.println("알 수 없는 에러");
            return resNull(1999,"복합적인 에러가 발생했습니다.");
        }
    }

    /**
     * 이메일 완전 삭제 메소드
     * @param emails List타입의 이메일 코드
     */
    @DeleteMapping("/delete-mail")
    public ResponseEntity<ResponseDTO> deleteMail(@RequestBody List<EmailDTO> emails){
        try{
            emailService.deleteEmail(emails);
            return res("성공",null);
        } catch (EmptyResultDataAccessException e){
            return resNull(1005,"삭제하려는 이메일이 존재하지 않습니다.");
        } catch (Exception e){
            return resNull(1999,"복합적인 에러가 발생했습니다.");
        }
    }

    /**
     * 임시 저장한 메일을 불러오는 메소드
     * @return 응답
     */
    @GetMapping("/temporary")
    public ResponseEntity<ResponseDTO> readTemporary(){
        //유저 코드 넣어주면서 임시저장한 거 찾아오기
        List<EmailDTO> emailDTO = emailService.findByEmailSender(new EmployeeDTO(1L));
        return res("성공",emailDTO);
    }
//    @PostMapping("/updateEmailSchedule")
//    public String updateEmailSchedule(@RequestBody ScheduleDTO schedule) {
//        //구현 필요
//
//        //LocalDate date = LocalDate.parse(schedule.getDate(), DateTimeFormatter.ISO_DATE);
//        //LocalTime time = LocalTime.parse(schedule.getTime(), DateTimeFormatter.ISO_TIME);
//        //LocalDateTime scheduledDateTime = LocalDateTime.of(date, time);
//
//        //emailService.updateScheduledTime(scheduledDateTime); // 서비스 메소드를 호출하여 업데이트
//
//        //return res("해보자",);
//        return null;
//    }






    /**
     * 에러를 갖고 응답하는 메소드
     * @param errorCode 에러코드
     * @param message 메세지
     * @return 에러코드,메세지,null 로 응답
     */
    private ResponseEntity<ResponseDTO> resNull(int errorCode, String message){
        return ResponseEntity.ok().body(new ResponseDTO(errorCode,message,null));
    }

    /**
     * 정상적으로 응답하는 메소드
     * @param msg 메세지
     * @param data 보낼 데이터
     * @return 200, 메세지, 보낼데이터 로 응답
     */
    private ResponseEntity<ResponseDTO> res(String msg,Object data){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,msg,data));
    }
    private String getId(String email){
        int index = email.indexOf("@"); //처음으로 @가 나오는 인덱스 (없으면 -1 반환)
        if(index != 1){ //있으면
            return email.substring(0,index);    //잘라서 갖다 줌
        } else {
            return "에러";
        }
    }
}
