package com.wittypuppy.backend.mail.controller;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.config.scheduler.DynamicTaskScheduler;
import com.wittypuppy.backend.mail.dto.EmailDTO;
import com.wittypuppy.backend.mail.dto.EmployeeDTO;
import com.wittypuppy.backend.mail.service.EmailService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
//com.wittypuppy.backend.Employee.dto.EmployeeDTO
@RequestMapping("/mail")
@RestController
public class MailController {
    private final EmailService emailService;
    private final SimpMessagingTemplate simp;
    private final DynamicTaskScheduler dynamicTaskScheduler;

    public MailController(EmailService emailService, SimpMessagingTemplate simp, DynamicTaskScheduler dynamicTaskScheduler) {
        this.emailService = emailService;
        this.simp = simp;
        this.dynamicTaskScheduler = dynamicTaskScheduler;
    }


    @GetMapping("unread-email-count")
    public ResponseEntity<ResponseDTO> countUnreadEmail(@AuthenticationPrincipal com.wittypuppy.backend.Employee.dto.EmployeeDTO principal){
        System.out.println("principal = " + principal);
        return null;
    }

    /**
     * 일반 메일 전송
     * @param email 보내는 이메일DTO 객체
     */
    @MessageMapping("/mail/alert/send")
    public void mailAlert(@Payload EmailDTO email){
        System.out.println("제발 와라");
        simp.convertAndSend("/topic/mail/alert/"+1,    //하드코딩 1
                emailService.sendMail(setDefault(email),"send"));
    }
    /**
     *  임시 저장
     * @param email 사용자가 메일 쓰기 중 입력한 데이터
     * @param status send, temporary, reserve 로 나뉜다.(일반, 임시저장, 예약)
     * @return 응답
     */
    @PostMapping("send-mail")
    public ResponseEntity<ResponseDTO> sendMail(@RequestBody EmailDTO email,@RequestParam String status) {
        //에러 처리 하셈
        EmailDTO result = emailService.sendMail(setDefault(email),status);

        return res("임시저장에 성공했습니다.", result);
    }
    //예약 메일 등록 후 예약 처리
    @GetMapping("send-reserve-mail")
    public ResponseEntity<ResponseDTO> test(@RequestBody EmailDTO emailDTO){

        EmailDTO result = emailService.sendReserveMail(setDefault(emailDTO));
        Long emailCode = result.getEmailCode();

        System.out.println("예약한 시간 : "+emailDTO.getEmailReservationTime());
        dynamicTaskScheduler.scheduleTask(emailDTO.getEmailReservationTime(),emailCode);
        return res("예약 메일이 정상적으로 등록되었습니다.",null);
    }

    @GetMapping("/find-receive-mail")
    public ResponseEntity<ResponseDTO> findReceiveMail(@RequestParam String condition){
        System.out.println(condition);
        List<EmailDTO> emailList = emailService.findReceiveMail(condition);
        if(condition.equals("temporary")){
            for(EmailDTO email : emailList){
                email.setEmailTitle("[임시저장] "+email.getEmailTitle());
            }
        }
        if(condition.equals("reserve")){
            for(EmailDTO email : emailList){
                email.setEmailTitle("[예약메일] "+email.getEmailTitle());
            }
        }
        if(condition.equals("trash")){
            for(EmailDTO email : emailList){
                email.setEmailTitle("[휴지통] "+email.getEmailTitle());
            }
        }
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        SimpleDateFormat outputFormat2 = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");

        if(emailList == null){
            return resNull(1003,"받은 이메일이 없습니다.");
        }
        for(EmailDTO email : emailList) {
            try {
                Date date = inputFormat.parse(email.getEmailSendTime());
                String formattedDate = outputFormat.format(date);
                email.setEmailSendTime(formattedDate);
            } catch (ParseException e) {
                try {
                    Date date = inputFormat2.parse(email.getEmailSendTime());
                    String formattedDate2 = outputFormat2.format(date);
                    email.setEmailSendTime(formattedDate2);
                } catch (ParseException ex) {
                    System.out.println(ex);
                    return null;
                }

            }

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

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/toggle-important")
    public ResponseEntity<ResponseDTO> updateStatus(@RequestParam Long emailCode, @RequestParam String emailStatus){
        EmailDTO emailDTO = emailService.findById(emailCode);
        System.out.println(emailDTO);
        EmailDTO result = emailService.updateStatus(emailDTO,emailStatus);
        return res("성공",result);
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
    @GetMapping("/find-a-mail")
    public ResponseEntity<ResponseDTO> findAMail(@RequestParam Long emailCode){
        EmailDTO email = emailService.findById(emailCode);
        return res("메일을 검색했습니다.",email);
    }
    /**
     * 이메일을 옵션에 따라 검색하는 메서드
     * @param word 검색어
     * @param option 옵션(title,content,sendTime,sender)
     * @return 응답
     */
    @GetMapping("/find-email")
    public ResponseEntity<ResponseDTO> findEmail(@RequestParam String word,@RequestParam String option){
        EmployeeDTO me = new EmployeeDTO(1L);
        List<EmailDTO> emails = new ArrayList<>();
        System.out.println("프론트에서 가져온 word는 : "+word);
        System.out.println("프론트에서 가져온 option은 : "+option);
        Long receiver = 1L;
        emails = switch (option) {
            case "title" -> emailService.findByEmailTitle(word,me);
            case "receiver" -> emailService.findAllByEmailSender(word,receiver);
            default -> emails;
        };
        if(emails == null){
            return resNull(1100,"검색된 메일이 없습니다.");
        }
        return res("메일을 검색했습니다.",emails);
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

    /**
     * 이메일 눌렀을 때 읽음으로 바꿔주는 메섣,
     * @param emailCode 이메일코드
     * @return 응답
     */
    @PutMapping("/read-stats-y")
    public ResponseEntity<ResponseDTO> updateReadStatus(@RequestParam Long emailCode){
        EmailDTO emailDTO = emailService.findById(emailCode);
        emailDTO.setEmailReadStatus("Y");
        emailDTO = emailService.updateEmailReadStatus(emailDTO);
        return res("성공적으로 읽음",emailDTO);
    }
    @GetMapping("/non-read-email")
    public ResponseEntity<ResponseDTO> findByEmailReadStatus(){
        EmployeeDTO user = new EmployeeDTO(1L,"wjdwltjq8482");
        List<EmailDTO> emailDTO = emailService.findByEmailReadStatus(user);
        return res("성공",emailDTO);
    }









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
    private EmailDTO setDefault(EmailDTO email){
        String receiverId = getId(email.getEmailReceiver().getEmployeeId());
        email.setEmailReceiver(emailService.findByEmployeeCode(receiverId));    //가져갈 객체에 받는 사람 저장

        email.setEmailSender(new EmployeeDTO(1L,"inji2349"));//보내는 사람 하드코딩

        email.setEmailSendTime(new Date().toString());                            //보낼 시간 현재로 저장
        email.setEmailReadStatus("N");                                          //읽지 않음으로 저장
        email.setEmailStatus("send");                                            //이메일 상태 기본으로
        return email;
    }
}
