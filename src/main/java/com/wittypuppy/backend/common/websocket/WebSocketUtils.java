package com.wittypuppy.backend.common.websocket;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketUtils {
    @GetMapping("/get-user-code")
    public ResponseEntity<ResponseDTO> getUserCode(@AuthenticationPrincipal User user){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"유저 정보 가져오기 성공",user));
    }
}
