package com.wittypuppy.backend.Employee.service;


import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.Employee.repository.EmployeeRepository;
import com.wittypuppy.backend.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;
@Service
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    private final JavaMailSender javaMailSender;

    private final TokenUtils tokenUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper, JavaMailSender javaMailSender, TokenUtils tokenUtils) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.javaMailSender = javaMailSender;
        this.tokenUtils = tokenUtils;
    }

    public User selectMyInfo(String employeeId) {
        log.info("[MemberService]  selectMyInfo   Start =============== ");

        LoginEmployee employee = employeeRepository.findByEmployeeId(employeeId);
        log.info("[employeeService]  {} =============== ", employee);
        log.info("[employeeService]  selectMyInfo   End =============== ");
        return modelMapper.map(employee, User.class);
    }


    //보낼 사용자 이메일의 정보 내가 보내는거면 내 이메일
    private void sendPasswordSearchEmail(String to, String temporaryPassword) {        System.out.println("비번 변경 서비스 시작=====================================");
        System.out.println("사용자의 이메일 정보 =====================================");

        SimpleMailMessage message = new SimpleMailMessage();
        String from = "WittyFlow <kimsungmin3313@gmail.com>";

        message.setFrom(from);
        message.setTo(to);
        message.setSubject("임시 비밀번호 발급");

        message.setText("임시 비밀번호 : " + temporaryPassword);

        javaMailSender.send(message);
    }


    @Transactional
    public User sendSearchPwd(String employeeId, String employeeEmail) throws UserPrincipalNotFoundException {
        Optional<LoginEmployee> employee = employeeRepository.findByEmployeeIdAndEmployeeEmail(employeeId, employeeEmail);
        System.out.println("비번 변경 서비스 시작=====================================");
        System.out.println("employee값이 들어있는지 확인용 = " + employee);
        if (employee.isPresent()) {
            //여기서 임시 비밀번호를 생성함
            String temporaryPassword = TokenUtils.randomString();//토큰유틸에 만들어놓음
            System.out.println("temporaryPassword 임시비밀번호 출력확인용 = " + temporaryPassword);
            //이메일로 임시 비밀번호 전송
            sendPasswordSearchEmail(employeeEmail, temporaryPassword);
            //사용자 비밀번호 업데이트
            System.out.println(" 비번 업데이트 시작=====================================");
//            LoginEmployee loginEmployee = employee.get();//get은 optional클래스에서 사용되는 메거드 중 하나
            System.out.println("passwordEncoder.encode(temporaryPassword)"+passwordEncoder.encode(temporaryPassword));
            //이게 출력이 안되었던 이유는 bean에 passencoder를 등록해줬어야 되었는데 안해줘서 값을 아예 못가져왔는데
            //위에서 autowired를 passencoder에 넣어주니까 값을 잘 들고왔다 메일은 보내졌는데 메일이 보내지고 나서 값을 잃었던 것

            System.out.println(" 비번 업데이트 시작에서 정보 가져오기=====================================");

//            loginEmployee.setEmployeePassword(passwordEncoder.encode(temporaryPassword)); //passwordEncoder 사용
            employee.get().setEmployeePassword(passwordEncoder.encode(temporaryPassword));  //엔티티로 해야됨
            System.out.println(" 비번 변경 서비스 거의 다옴 =====================================");

            employeeRepository.save(employee.get());

            System.out.println(" 비번 변경 서비스 끝=====================================");

        return modelMapper.map(employee.get(), User.class);//일단 오류는 안뜨게함
        } else {
            throw new UserPrincipalNotFoundException("사용자 정보를 찾을 수 없습니다.");
        }
    }







}
