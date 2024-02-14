package com.wittypuppy.backend.Employee.controller;


import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.Employee.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인 스웨거 연동")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }


//    회원가입에 관한 건 어떻게 하지 상태값 없애고 퇴직날짜가 null이면 회사를 다니고 있는 상태이고 퇴직날짜가 존재하면 탈퇴한 상태

    @Tag(name="회원가입", description = "사원 계정 회원가입을 통한 생성")
    @PostMapping("/signup")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> signup(@RequestBody User user, @AuthenticationPrincipal User principal){
        System.out.println("principal 회원가입 컨트롤러에서 출력  = " + principal);
        // 멤버의 기본 상태값 설정
        user.setEmployeeRetirementDate(null);//퇴직일을 null로 처리
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.CREATED, "회원가입 성공", authService.signup(user)));

    }
}
