package com.wittypuppy.backend.util;
import com.wittypuppy.backend.Employee.dto.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 토큰을 관리하기 위한 utils 모음 클래스
 *  yml -> jwt-key, jwt-time 설정이 필요하다.
 *  jwt lib 버전 "io.jsonwebtoken:jjwt:0.9.1" 사용
 * */

@Component
@Slf4j
public class TokenUtils {

    private static String jwtSecretKey;
    private static Long tokenValidateTime;

    private static final SecureRandom secureRandom = new SecureRandom();



    @Value("${jwt.key}")
    public void setJwtSecretKey(String jwtSecretKey) {
        TokenUtils.jwtSecretKey = jwtSecretKey;
    }

    @Value("${jwt.time}")
    public void setTokenValidateTime(Long tokenValidateTime) {
        TokenUtils.tokenValidateTime = tokenValidateTime;
    }

    /**
     * header의 token을 분리하는 메서드
     * @param header: Authrization의 header값을 가져온다.
     * @return token: Authrization의 token 부분을 반환한다.
     * */
    public static String splitHeader(String header){
        if(!header.equals("")){
            return header.split(" ")[1];  //[0]은 bearer이므로 [1]번째인 실제 토큰 값을 가져온다.
        }else{
            return null;
        }
    }

    /**
     * 유용한 토큰인지 확인하는 메서드
     * @param token : 토큰
     * @return boolean : 유효 여부
     * @throws ExpiredJwtException, {@link JwtException} {@link NullPointerException}
     * */


    public static boolean isValidToken(String token){

        try{
            Claims claims = getClaimsFromToken(token);
            return true;
        }catch (ExpiredJwtException e){
            e.printStackTrace();
            return false;
        }catch (JwtException e){
            e.printStackTrace();
            return false;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 토큰을 복호화 하는 메서드 (알아보니까 컴퓨터가 알아보게 파싱해서 서명된 jwt객체를 얻어서 키로 인식 )
     * @param token
     * @return Claims
     * */

    public static Claims getClaimsFromToken(String token){
        // 1. JWT 토큰을 파싱하고, 서명 키(Jwt를 생성할 때 사용한 비밀 키)를 설정합니다.
        // 2. 파싱한 토큰을 이용하여 Jws(서명된 JWT) 객체를 얻습니다.
        // 3. Jws 객체에서 본문(Claims)를 추출하여 반환합니다.
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                .parseClaimsJws(token).getBody();
    }


    /**
     * token을 생성하는 메서드
     * @param employee 사용자객체
     * @return String - token
     * */

    public static String generateJwtToken(User employee) {
        Date expireTime = new Date(System.currentTimeMillis() + tokenValidateTime);


        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(employee))
                .setSubject(employee.getEmployeeId())
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(expireTime);


        return builder.compact();//여기서 compact는 최종적인 문자열 형태로 변환하는 역할을 한다.
    }

    /**
     * token의 header를 설정하는 부분이다.
     * @return Map<String, Object> - header의 설정 정보
     * */

    private static Map<String, Object> createHeader(){
        Map<String, Object> header = new HashMap<>();

        header.put("type", "jwt");
        header.put("alg", "HS256");  //토큰이 안전하게 생성되었음을 확인 할 수 있도록 해준다. 데이터의 무결성을 검증
        header.put("date", System.currentTimeMillis());

        return header;
    }

    /**
     * 사용자 정보를 기반으로 클레임을 생성해주는 메서드
     *
     * @param employee - 사용자 정보
     * @return Map<String, Object> - cliams 정보
     * */
    private static Map<String, Object> createClaims(User employee){
        Map<String, Object> claims = new HashMap<>();

        claims.put("employeeName", employee.getEmployeeName());
        claims.put("employeeRole", employee.getEmployeeRole());
        claims.put("employeeEmail", employee.getEmployeeEmail());
        claims.put("empCode", employee.getEmployeeCode());
        claims.put("employeeId", employee.getEmployeeId());
        claims.put("departmentName", employee.getDepartment().getDepartmentName());
        claims.put("departmentCode", employee.getDepartment().getDepartmentCode());
        claims.put("empGroupCode", employee.getDepartment().getParentDepartmentCode());
        //클래임은 문자열 키와 그에 해당하는 값으로 이루어진 맵 형태이다.

        return claims;
    }
    public String getUserId(String token){
        Claims claims = Jwts.parser()   //JWT를 파싱하기 위해서
                .setSigningKey(jwtSecretKey)    //생성된 시크릿 키
                .parseClaimsJws(token.replace("Bearer ", ""))   //Bearer빼고 파싱함
                .getBody(); //바디를 가져옴 사용자 정보 같은 거 여기 있음
        return claims.getSubject(); //아이디 나와라
    }
    public int getUserEmployeeCode(String token){
        Claims claims = Jwts.parser()   //JWT를 파싱하기 위해서
                .setSigningKey(jwtSecretKey)    //생성된 시크릿 키
                .parseClaimsJws(token.replace("Bearer ", ""))   //Bearer빼고 파싱함
                .getBody(); //바디를 가져옴 사용자 정보 같은 거 여기 있음
        return (Integer)claims.get("empCode"); //아이디 나와라
    }
    /**
     * JWT 서명을 발급해주는 메서드이다.
     *
     * @return key
     * */
    private static Key createSignature(){
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        System.out.println("jwtSecretkey 나오는지 확인하기 "+ jwtSecretKey);
        //Base64로 인코딩된 비밀 키인 jwtSecretKey를 DatatypeConverter.parseBase64Binary사용해서 Base64형식의 문자열을 이진 데이터로 디코딩합니다.
        return new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
        //여기서 secretBytes는 디코딩된 비밀키를 나타내고, SignatureAlgorithm.HS256.getJcaName()이라는 알고리즘을 지정해서
        //이 알고리즘에 사용될 수 잇는 key객체를 생성하여 반환
    }

//    /***
//     * 토큰이나 임시 비밀번호를 위한 랜덤 문자열 생성 매서드이다.
//     * @return 랜덤 문자열
//     */
    public static String randomString() {
        byte[] randomBytes = new byte[8];
        try {
            secureRandom.nextBytes(randomBytes);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리 - 원하는 방식으로 처리하도록 수정 가능
        }
        System.out.println("randomBytes 출력 = " + randomBytes);
        log.info("ramdom번호 출력 나오냐", randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }



}
