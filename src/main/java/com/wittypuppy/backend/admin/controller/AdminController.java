package com.wittypuppy.backend.admin.controller;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.admin.dto.*;
import com.wittypuppy.backend.admin.entity.Profile;
import com.wittypuppy.backend.admin.service.AdminService;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.admin.dto.EmailDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;
import org.hibernate.QueryTimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.wittypuppy.backend.util.FileUploadUtils.saveFile;

@RestController
@RequestMapping("admin")
public class AdminController {

    private final AdminService adminService;
    private final SimpMessagingTemplate simp;

    @Value("${file.file-dir}")
    private String FILE_DIR;

    public AdminController(AdminService adminService, SimpMessagingTemplate simp) {
        this.adminService = adminService;
        this.simp = simp;
    }


    @MessageMapping("/mail/alert/admin/send")
    public void mailAlert(@Payload EmailDTO email, SimpMessageHeaderAccessor accessor){
        //관리자가 보냈음
        email.setEmailSender(new EmployeeDTO(15L));
        email.setEmailStatus("send");
        email.setEmailReadStatus("N");

        if(email.getStatus().equals("all")){    //조직이 all이라면
            List<EmployeeDTO> employeeDTO = adminService.sendMailAll(email);
            for(EmployeeDTO emp : employeeDTO){
                simp.convertAndSend("topic/mail/alert/"+emp.getEmployeeCode(),email);
            }
        } else if(!email.getStatus().isEmpty()){    //조직이 비어있다면
            List<EmployeeDTO> employeeDTO = adminService.sendDepartmentMail(email);
            for(EmployeeDTO emp : employeeDTO){
                simp.convertAndSend("topic/mail/alert/"+emp.getEmployeeCode(),email);
            }
        }
    }
    @MessageMapping("/mail/alert/admin/send2")
    public void mailAlert2(@Payload EmailDTO email, SimpMessageHeaderAccessor accessor){
        //관리자가 보냈음
        System.out.println("여2"+email.getStatus2());
        email.setEmailSender(new EmployeeDTO(15L));
        email.setEmailStatus("send");
        email.setEmailReadStatus("N");
        List<EmployeeDTO> employeeDTOS = adminService.sendMailAll2(email);
        for(EmployeeDTO emp : employeeDTOS){
            simp.convertAndSend("topic/mail/alert/"+emp.getEmployeeCode(),email);
        }
    }
    @GetMapping("/get-department")
    public ResponseEntity<ResponseDTO> getDepartment(){
        List<DepartmentDTO> departmentDTO = adminService.getDepartment();
        for(DepartmentDTO department : departmentDTO){
            System.out.println(department);
        }
        return res("조직 조회 성공",departmentDTO);
    }
    /**
     * 유저의 모든 정보를 가져오는 메서드
     * @param employeeDTO 유저 코드를 받음
     * @return 에러코드 9000 = 유저 아이디를 찾을 수 없음
     */
    @GetMapping("/user-info")
    public ResponseEntity<ResponseDTO> getUserInfo(@RequestBody EmployeeDTO employeeDTO) {
        try {
            employeeDTO = adminService.getUserInfo(employeeDTO);
            //주민번호 뒤 6자리 * 로
            employeeDTO.setEmployeeResidentNumber(employeeDTO.getEmployeeResidentNumber().substring(0, employeeDTO.getEmployeeResidentNumber().length() - 6) + "******");

        } catch (NoResultException e){
            resNull(9000,"유저 아이디를 찾을 수 없습니다.");
        } catch (IllegalArgumentException e){
            resNull(9001,"잘못된 인수가 전달되었습니다.");
        } catch (Exception e){
            return resNull(9999,"알 수 없는 에러가 발생했습니다.");
        }
        return res("유저 정보 조회에 성공했습니다.",employeeDTO);
    }

    /**
     * 수정할 때 유저 전체 정보 받아야 함.
     * 부서,보직,퇴사,휴가일 수, 권한 모두 설정 가능
     * (에러 처리 필요)
     * @param employeeDTO 유저 모든 정보
     */
    @PutMapping("/user-infor-update")
    public ResponseEntity<ResponseDTO> updateUserInfo(@RequestBody EmployeeDTO employeeDTO){
        try {
            employeeDTO = adminService.updateUserInfo(employeeDTO);
        } catch (DataIntegrityViolationException e){
            resNull(9002,"외래키 참조 에러 입니다.\n 존재하지 않는 부서, 직급을 사용했습니다.");
        } catch (TransactionSystemException e){
            resNull(9003, "트랜젝션 에러입니다.\n 정상적으로 데이터가 수정되지 않았습니다.");
        } catch (QueryTimeoutException e){
            resNull(9100,"시간 초과 에러입니다.\n 데드락이 의심됩니다.");
        } catch (Exception e){
            resNull(9999,"알 수 없는 에러가 발생했습니다.");
        }
        return res("유저 정보 수정에 성공했습니다.",employeeDTO);
    }
//    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/insert-profile")
    public ResponseEntity<ResponseDTO> insertProfile(MultipartFile profile, @AuthenticationPrincipal User user){
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setProfileOgFile(profile.getOriginalFilename());
        profileDTO.setProfileRegistDate(LocalDateTime.now());
        profileDTO.setProfileDeleteStatus("N");
        String fileName = UUID.randomUUID().toString().replace("-", "");
        try {
            //saveFile 메서드 : util패키지에 static으로 존재함
            profileDTO.setProfileChangedFile(saveFile(FILE_DIR, //인자 1 : 파일 저장 위치
                    fileName,   //인자 2 : 아까 랜덤하게 만든 새로운 파일 이름
                    profile));     //MultipartFile의 i 번째 (가져온 첨부파일)
        }catch (IOException e){     //저장하다가 에러나면?
            System.err.println(e.getMessage()); //메세지 출력
        }
        ProfileDTO result = adminService.insertProfile(profileDTO,user);
        return res("굳",result);
    }
    /**
     * 유저 회원 가입 시키기
     * (비밀번호 암호화 하기)
     * @param userDTO 유저 정보, 학위, 경력을 받음
     * @return 9002,9004 에러 있음
     */
    @PostMapping("/create-user")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody CreateUserDTO userDTO){
        System.out.println("createUser 처리 중 : "+userDTO);
        try {
            EmployeeDTO employeeDTO = adminService.createUser(userDTO.getEmployee());
            userDTO.getEducation().setEmployeeCode(employeeDTO.getEmployeeCode());
            userDTO.getCareer().setEmployeeCode(employeeDTO.getEmployeeCode());

            EducationDTO educationDTO = adminService.createUserEducation(userDTO.getEducation());
            CareerDTO careerDTO = adminService.createUserCareer(userDTO.getCareer());
            userDTO.setEmployee(employeeDTO);
            userDTO.setCareer(careerDTO);
            userDTO.setEducation(educationDTO);
        } catch (DataIntegrityViolationException e){
            resNull(9002,"외래키 참조 오류입니다.");
        } catch (EntityExistsException e){
            resNull(9004,"기본 키가 중복됩니다.");
        }

        return res("유저 추가에 성공했습니다.",userDTO);
    }

    /**
     * 비밀번호 초기화 메소드 (구현 중)
     * @param employeeDTO 수정할 유저의 코드를 받아 옴
     * @return 바뀐 비밀번호
     */
    @PutMapping("reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody EmployeeDTO employeeDTO){
        return res("구현 중",employeeDTO);
    }
    @GetMapping("show-need-to-allow-board")
    public ResponseEntity<ResponseDTO> showNeedAllowBoard(){
        List<BoardDTO> boardDTO = adminService.showNeedAllowBoard();
        return res("성공적인 조회",boardDTO);
    }
    @PutMapping("allow-board")
    public ResponseEntity<ResponseDTO> allowBoardAccessStatus(@RequestParam Long boardCode){
        BoardDTO boardDTO = adminService.findById(boardCode);
        boardDTO.setBoardAccessStatus("Y");
        BoardDTO result = adminService.allowBoard(boardDTO);
        return res("성공적으로 허가햿습니다.",result);
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

    //연차 인서트


}
