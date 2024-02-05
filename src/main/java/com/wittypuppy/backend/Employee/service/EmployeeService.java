package com.wittypuppy.backend.Employee.service;


import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.Employee.repository.EmployeeRepository;
import com.wittypuppy.backend.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Base64;
import java.util.Optional;
@Service
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    private final JavaMailSender javaMailSender;

    private final TokenUtils tokenUtils;

    private PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper, JavaMailSender javaMailSender, TokenUtils tokenUtils) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.javaMailSender = javaMailSender;
        this.tokenUtils = tokenUtils;
    }

    public EmployeeDTO selectMyInfo(String employeeId) {
        log.info("[MemberService]  selectMyInfo   Start =============== ");

        LoginEmployee employee = employeeRepository.findByEmployeeId(employeeId);
        log.info("[employeeService]  {} =============== ", employee);
        log.info("[employeeService]  selectMyInfo   End =============== ");
        return modelMapper.map(employee, EmployeeDTO.class);
    }


    //보낼 사용자 이메일의 정보 내가 보내는거면 내 이메일
    private void sendPasswordSearchEmail(String to, String temporaryPassword) {        System.out.println("비번 변경 서비스 시작=====================================");
        System.out.println("사용자의 이메일 정보 =====================================");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kimsungmin3313@gmail.com");
        message.setTo(to);
        message.setSubject("mlzb ywni imuh unpg");
        message.setText("Your temporary password is: " + temporaryPassword);

        javaMailSender.send(message);
    }


    @Transactional
    public EmployeeDTO sendSearchPwd(String employeeId, String employeeEmail) throws UserPrincipalNotFoundException {
        Optional<LoginEmployee> employee = employeeRepository.findByEmployeeIdAndEmployeeEmail(employeeId, employeeEmail);
        System.out.println("비번 변경 서비스 시작=====================================");
        if (employee.isPresent()) {
            //여기서 임시 비밀번호를 생성함
            String temporaryPassword = TokenUtils.randomString();//토큰유틸에 만들어놓음
            System.out.println("temporaryPassword 임시비밀번호 출력확인용 = " + temporaryPassword);
            //이메일로 임시 비밀번호 전송
            sendPasswordSearchEmail(employeeEmail, temporaryPassword);
            //사용자 비밀번호 업데이트
            System.out.println(" 비번 업데이트 시작=====================================");
            LoginEmployee loginEmployee = employee.get();//get은 optional클래스에서 사용되는 메거드 중 하나
            System.out.println(" 비번 업데이트 시작에서 정보 가져오기=====================================");

            loginEmployee.setEmployeePassword(passwordEncoder.encode(temporaryPassword)); //passwordEncoder 사용
            System.out.println(" 비번 변경 서비스 거의 다옴 =====================================");

            employeeRepository.save(loginEmployee);

            System.out.println(" 비번 변경 서비스 끝=====================================");

        return modelMapper.map(loginEmployee, EmployeeDTO.class);//일단 오류는 안뜨게함
        } else {
            throw new UserPrincipalNotFoundException("사용자 정보를 찾을 수 없습니다.");
        }
    }







}
