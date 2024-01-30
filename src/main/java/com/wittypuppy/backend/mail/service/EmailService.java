package com.wittypuppy.backend.mail.service;

import com.wittypuppy.backend.mail.dto.EmailDTO;
import com.wittypuppy.backend.mail.dto.EmployeeDTO;
import com.wittypuppy.backend.mail.entity.Email;
import com.wittypuppy.backend.mail.entity.Employee;
import com.wittypuppy.backend.mail.repository.MailEmailRepository;
import com.wittypuppy.backend.mail.repository.MailEmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {
    private final ModelMapper modelMapper;
    private final MailEmailRepository emailRepository;
    private final MailEmployeeRepository employeeRepository;

    public EmailService(ModelMapper modelMapper, MailEmailRepository emailRepository, MailEmployeeRepository employeeRepository) {
        this.modelMapper = modelMapper;
        this.emailRepository = emailRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<EmailDTO> findReceiveMail(String emailStatus) {
        List<Email> emailList = emailRepository.findReceiveMail(1L, emailStatus);

        return emailList.stream().map(email->modelMapper.map(email,EmailDTO.class)).toList();
    }
    public List<EmailDTO> findSendMail(String emailStatus) {
        List<Email> emailList = emailRepository.findSendMail(1L, emailStatus);

        return emailList.stream().map(email->modelMapper.map(email,EmailDTO.class)).toList();
    }

    /**
     * 사용자의 ID로 EmployeeCode를 찾아오는 메소드
     * @param emailReceiverEmployee 유저의 ID
     * @return  tbl_employee테이블에서 유저의 모든 정보를 가져 옴
     */
    public EmployeeDTO findByEmployeeCode(String emailReceiverEmployee) {

        Employee employee = employeeRepository.findByEmployeeId(emailReceiverEmployee);

        return modelMapper.map(employee,EmployeeDTO.class);
    }

    @Transactional
    public EmailDTO sendMail(EmailDTO email, String status){
        switch (status){    //여기에 상태에 맞춰서 처리하자.
            case "send":break;
            case "temporary":   //임시저장
                email.setEmailStatus("temporary");break;  //별도 처리 필요 없음
            case "reserve":     //예약
                email.setEmailStatus("reserve");break;    //스케쥴링ㄱ 시간 되면 send로 바꿔주면 됨

            default: return null;
        }
        Email emailEntity = modelMapper.map(email,Email.class);    //엔티티로 변환 됨


        return modelMapper.map(emailRepository.save(emailEntity),EmailDTO.class);

    }


//    public EmailDTO findByEmailCode(Long emailCode) {
//        Email email = emailRepository.findByEmailCode(emailCode);
//        System.out.println("---------------->"+  email);
//        EmailDTO result = modelMapper.map(email, EmailDTO.class);
//        return result;
//    }
    @Transactional
    public List<EmailDTO> updateEmailStatus(List<EmailDTO> emailDTO) {
        List<Email> emailEntity = convert(emailDTO,Email.class);
        emailRepository.saveAll(emailEntity);
        return convert(emailEntity, EmailDTO.class);
    }

    @Transactional
    public void deleteEmail(List<EmailDTO> emails) {
        List<Email> emailListEntity = emails.stream().map(email->modelMapper.map(email,Email.class)).toList();
        List<Long> emailList = new ArrayList<>();
        for(Email email : emailListEntity){
            emailList.add(email.getEmailCode());
        }
        emailRepository.deleteAllById(emailList);
    }

    public List<EmailDTO> findByAllEmailCode(List<EmailDTO> emailDTOs) {
        List<Email> emailEntity = convert(emailDTOs,Email.class);
        List<Long> emailEntityCode = new ArrayList<>();
        for(Email email : emailEntity){
            emailEntityCode.add(email.getEmailCode());
        }
        List<Email> emails = emailRepository.findByEmailCodeIn(emailEntityCode);
        return convert(emails, EmailDTO.class);
    }
    public List<EmailDTO> findByEmailSender(EmployeeDTO employeeDTO) {
        System.out.println("employeeDTO.getEmployeeCode() = " + employeeDTO.getEmployeeCode()); //2
        Employee employeeCode = modelMapper.map(employeeDTO,Employee.class);
        System.out.println("employeeEntity = " + employeeCode.getEmployeeCode());   //ㅇㅇ



        List<Email> emailEntity = emailRepository.findByEmailSenderAndEmailStatus(employeeCode,"temporary");
        return convert(emailEntity,EmailDTO.class);
    }

    /**
     * DTO를 엔티티로, 엔티티를 DTO로 바꿔주는 메소드
     * @param list 바꿀 DTO또는 엔티티 데이터
     * @param targetClass 바꿀 클래스 예) EmailDTO.class
     * @return 변환된 값
     */
    private <S, T> List<T> convert(List<S> list, Class<T> targetClass) {
        return list.stream()
                .map(value -> modelMapper.map(value, targetClass))
                .collect(Collectors.toList());
    }


}
