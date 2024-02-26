package com.wittypuppy.backend.auth.handler;

import com.wittypuppy.backend.common.dto.AuthConstants;
import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.Employee.dto.TokenDTO;
import com.wittypuppy.backend.util.ConvertUtil;
import com.wittypuppy.backend.util.TokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Configuration
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        User employee  = ((User) authentication.getPrincipal());
        System.out.println("============나는야 : "+employee);
        HashMap<String, Object> responseMap = new HashMap<>();
        JSONObject jsonValue = null;
        JSONObject jsonObject;
        if(employee.getEmployeeRetirementDate() != null){   //null이면 일반 사원이고 Null이 아니면 탈퇴계정이다. 바꿔줌
            responseMap.put("userInfo", jsonValue);
            responseMap.put("status", 500);
            responseMap.put("message","탈퇴 상태인 계정입니다.");
        }else{

            String token = TokenUtils.generateJwtToken(employee);
            // tokenDTO response
            TokenDTO tokenDTO = TokenDTO.builder()
                                .employeeName(employee.getEmployeeName())
                                .employeeCode(employee.getEmployeeCode())
                                .employeeId(employee.getEmployeeId())
                                .deptCode(employee.getDepartment().getDepartmentCode())
                                .groupCode(employee.getDepartment().getParentDepartmentCode())
                                .accessToken(token)
                                .grantType(AuthConstants.TOKEN_TYPE)
                                .build();

            jsonValue = (JSONObject) ConvertUtil.convertObjectToJsonObject(tokenDTO);

            responseMap.put("userInfo", jsonValue);
            responseMap.put("status", 200);
            responseMap.put("message", "로그인 성공");
        }

        jsonObject = new JSONObject(responseMap);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonObject);
        printWriter.flush();
        printWriter.close();
    }
}
