package com.wittypuppy.backend.auth.filter;


import com.wittypuppy.backend.Employee.dto.AuthorityDTO;
import com.wittypuppy.backend.Employee.dto.DepartmentDTO;
import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.Employee.dto.EmployeeRoleDTO;
import com.wittypuppy.backend.common.dto.AuthConstants;
import com.wittypuppy.backend.util.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        /*
         * 권한이 필요없는 리소스
         * */

        List<String> roleLeessList = Arrays.asList(
                "/",
                "/auth/signup",
                "/auth/login",
                "/websocket",
                "/api/v1/employee/searchpwd",
                "/api/v1/project/projects/upload-image",
                "/swagger-ui/(.*)",        //swagger 설정
                "/swagger-ui/index.html",  //swagger 설정
                "/v3/api-docs",              //swagger 설정
                "/v3/api-docs/(.*)",         //swagger 설정
                "/swagger-resources",        //swagger 설정
                "/swagger-resources/(.*)",    //swagger 설정
                "/mail/download-attachment/(.*)",
                "/swagger-resources/(.*)",    //swagger 설정
                "/board/file-download/(.*)"
        );


        if (roleLeessList.stream().anyMatch(uri -> roleLeessList.stream().anyMatch(pattern -> Pattern.matches(pattern, request.getRequestURI())))) {
            chain.doFilter(request, response);
            return;
            //어떤 패턴과 일치하는지 확인하고, 일치하는 경우에는 권한 검사를 건너뛰고 다음 필터로 요펑을 전달하는 역할을 함
        }

        String header = request.getHeader(AuthConstants.AUTH_HEADER);

        try {
            if (header != null && !header.equalsIgnoreCase("")) {
                String token = TokenUtils.splitHeader(header);
                System.out.println("token ====================== " + token);

                if (TokenUtils.isValidToken(token)) {
                    Claims claims = TokenUtils.getClaimsFromToken(token);
                    System.out.println("claims ===================== " + claims);
                    User authentication = new User();
                    authentication.setEmployeeName(claims.get("employeeName").toString());
                    authentication.setEmployeeEmail(claims.get("employeeEmail").toString());
                    authentication.setEmployeeCode((Integer) claims.get("empCode"));
                    authentication.setEmployeeId((String) claims.get("employeeId"));
                    DepartmentDTO departmentDTO = new DepartmentDTO();
                    departmentDTO.setDepartmentCode((Integer) claims.get("departmentCode"));
                    departmentDTO.setDepartmentName((String)claims.get("departmentName"));
                    departmentDTO.setParentDepartmentCode((Integer) claims.get("empGroupCode"));
                    authentication.setDepartment(departmentDTO);
                    System.out.println("claims ==================== " + claims.get("employeeRole"));

                    // List<EmployeeRoleDTO> 설정 dto타입이라서 한 번 더 설정해줘야됨
                    List<EmployeeRoleDTO> employeeRoles = mapToEmployeeRolelist(claims.get("employeeRole"));
                    System.out.println("userRoles =-======================>>>> " + employeeRoles);
                    authentication.setEmployeeRole(employeeRoles);
                    System.out.println("userRoles =-======================>>>>22222222222 " + employeeRoles);

                    AbstractAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(authentication, token, authentication.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    chain.doFilter(request, response);
                } else {
                    throw new RuntimeException("토큰이 유효하지 않습니다.");
                }
            } else {

                throw new RuntimeException("토큰이 존재하지 않습니다.");
            }
        } catch (Exception e) {
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json");
//            PrintWriter printWriter = response.getWriter();
//            JSONObject jsonObject = jsonresponseWrapper(e);
//            printWriter.print(jsonObject);
//            printWriter.flush();
//            printWriter.close();
        }
    }

    private List<EmployeeRoleDTO> mapToEmployeeRolelist(Object employeeRoleObject) {
        List<EmployeeRoleDTO> employeeRoles = new ArrayList<>();
        if (employeeRoleObject instanceof List<?>) {
            for (Map<String, Object> roleMap : (List<Map<String, Object>>) employeeRoleObject) {
                EmployeeRoleDTO employeeRole = new EmployeeRoleDTO();
                employeeRole.setEmployeeCode((Integer) roleMap.get("employeeCode"));
                employeeRole.setAuthorityCode((Integer) roleMap.get("authorityCode"));

                Object authorityObject = roleMap.get("authority");
                employeeRole.setAuthority(AuthorityDTO.fromLinkedHashMap((LinkedHashMap<String, Object>) authorityObject));
                employeeRoles.add(employeeRole);
            }
            System.out.println("employeeRoles 니오냐 ================= " + employeeRoles);
        }
        return employeeRoles;
    }

    /**
     * 토큰 관련된 Exception 발생 시 예외 응답
     */
    private JSONObject jsonresponseWrapper(Exception e) {
        String resultMsg;
        if (e instanceof ExpiredJwtException) {
            resultMsg = "Token Expired";
        } else if (e instanceof SignatureException) {
            resultMsg = "TOKEN SignatureException Login";
        }
        // JWT 토큰내에서 오류 발생 시
        else if (e instanceof JwtException) {
            resultMsg = "TOKEN Parsing JwtException";
        }
        // 이외 JTW 토큰내에서 오류 발생
        else {
            e.printStackTrace();
            resultMsg = "OTHER TOKEN ERROR";
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("message", resultMsg);
        jsonMap.put("reason", e.getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);
        return jsonObject;
    }
}
