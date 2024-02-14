package com.wittypuppy.backend.auth.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wittypuppy.backend.Employee.dto.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /*
    * 필터는 일반적인 HTTP 요청 및 응답의 가로채기와 수정에 사용되고,
    * 인터셉터는 주로 컨트롤러 메서드의 호출 전후 동작을 추가하기 위해 사용됩니다.
    * */

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
    /**
     * 지정된 url 요청시 해당 요청을 가로채서 검증 로직을 수행하는 메서드
     * */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;

        try {
            authRequest = getAuthRequest(request);
            setDetails(request, authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.getAuthenticationManager().authenticate(authRequest);
    }
    /**
     * 사용자의 로그인 리소스 요청시 요청 정보를 임시 토큰에 저장하는 메서드
     *
     * @param request - httpServletRequest
     * @return UserPasswordAuthenticationToken
     * @throw Excpetion e
     * */
    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
        User user = objectMapper.readValue(request.getInputStream(), User.class);

        return new UsernamePasswordAuthenticationToken(user.getEmployeeId(), user.getPassword());
    }

}
