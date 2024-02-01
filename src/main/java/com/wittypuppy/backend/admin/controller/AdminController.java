package com.wittypuppy.backend.admin.controller;

import com.wittypuppy.backend.admin.dto.*;
import com.wittypuppy.backend.admin.service.AdminService;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    //직원의 상세정보를 조회해 보자
    //필요한 엔티티 employee, department, job
    //education, career

    /**
     * 유저의 모든 정보를 가져오는 메서드
     * @param employeeDTO 유저 코드를 받음
     * @return 에러코드 9000 = 유저 아이디를 찾을 수 없음
     */
    @GetMapping("/user-info")
    public ResponseEntity<ResponseDTO> getUserInfo(@RequestBody EmployeeDTO employeeDTO){
        try {
            employeeDTO = adminService.getUserInfo(employeeDTO);
        } catch (Exception e){
            return resNull(9000,"유저 아이디를 찾을 수 없습니다,");
        }
        return res("유저 정보 조회에 성공했습니다.",employeeDTO);
    }
    @PutMapping("/user-infor-update")
    public ResponseEntity<ResponseDTO> updateUserInfo(@RequestBody EmployeeDTO employeeDTO){
        //수정 정보를 받아와서
        //부서도 여기에서 수정하자
        //보직도 여기에서 수정
        //에듀케이션,커리어는 수정할 필요 x
        //퇴사, 휴가 일, 권한도 이거로 수정 가능
        System.out.println(employeeDTO);

        employeeDTO = adminService.updateUserInfo(employeeDTO);

        return res("유저 정보 수정에 성공했습니다.",employeeDTO);
    }

    @PostMapping("/create-user")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody CreateUserDTO userDTO){
        //유저 create하고
        EmployeeDTO employeeDTO = adminService.createUser(userDTO.getEmployee());
        //만들어진 유저 코드를 각 객체에 삽입
        userDTO.getEducation().setEmployeeCode(employeeDTO.getEmployeeCode());
        userDTO.getCareer().setEmployeeCode(employeeDTO.getEmployeeCode());

        //후에 등록한다.
        EducationDTO educationDTO = adminService.createUserEducation(userDTO.getEducation());
        CareerDTO careerDTO = adminService.createUserCareer(userDTO.getCareer());
        userDTO.setEmployee(employeeDTO);
        userDTO.setCareer(careerDTO);
        userDTO.setEducation(educationDTO);
        return res("유저 추가에 성공했습니다.",userDTO);
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
}
