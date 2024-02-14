package com.wittypuppy.backend.Employee.service;


import com.wittypuppy.backend.common.exception.DuplicatedMemberEmailException;
import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.Employee.entity.LoginEmployeeRole;
import com.wittypuppy.backend.Employee.repository.EmployeeRepository;
import com.wittypuppy.backend.Employee.repository.EmployeeRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EmployeeRoleRepository employeeRoleRepository;

    public AuthService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, EmployeeRoleRepository employeeRoleRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.employeeRoleRepository = employeeRoleRepository;
    }

    @Transactional   // DML작업은 Transactional 어노테이션 추가
    public Object signup(User user) {

        log.info("[AuthService] signup Start ==================================");
        log.info("[AuthService] memberDTO {} =======> ", user);

        /* 이메일 중복 유효성 검사(선택적) */
        if(employeeRepository.findByEmployeeEmail(user.getEmployeeEmail()) != null){ // 중복된 내용이 있으니 값을 가지고 온 것 (없으면 null)
            log.info("[AuthService] 이메일이 종복됩니다.");
            throw new DuplicatedMemberEmailException("이메일이 중복됩니다.");
        }

        LoginEmployee registEmployee = modelMapper.map(user, LoginEmployee.class);

        /* 1. TBL_EPLOYEE 테이블에 회원 insert */
        registEmployee = registEmployee.employeePassword(passwordEncoder.encode(registEmployee.getEmployeePassword())).build(); // 평문의 암호문자열을 암호화시켜서 전달
        LoginEmployee result1 = employeeRepository.save(registEmployee);    // 반환형이 int값이 아니다.
        log.info("[AuthService] result1 ================== {} ", result1);

        /* 2. TBL_EPLOYEE_ROLE 테이블에 회원별 권한 insert (현재 엔티티에는 회원가입 후 pk값이 없는상태) */
        // 일반 권한의 회원을 추가(AuthorityCode값이 2번)

        /* 엔티티에는 추가 할 회원의 pk값이 아직 없으므로 기존 회원의 마지막 회원 번호를 조회
        *  하지만 jpql에 의해 앞선 save와 jpql이 flush()로 쿼리와 함께 날아가고 회원이 이미 sequence객체 값 증가와 함께
        *  insert가 되어 버린다. -> 고로, maxMemberCode가 현재 가입하는 회원의 번호를 의미한다.
        * */
        LoginEmployeeRole registMemberRole = new LoginEmployeeRole(Math.toIntExact(result1.getEmployeeCode()), 1);
        LoginEmployeeRole result2 = employeeRoleRepository.save(registMemberRole);
        log.info("[AuthService] EmployeeInsert Result {}",
                        (result1 != null && result2 != null)? "회원 가입 성공" : "회원 가입 실패");

        log.info("[AuthService] signup End ==================================");

        return user;
    }


}
