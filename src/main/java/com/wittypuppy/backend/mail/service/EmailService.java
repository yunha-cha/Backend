package com.wittypuppy.backend.mail.service;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.mail.dto.EmailAttachmentDTO;
import com.wittypuppy.backend.mail.dto.EmailDTO;
import com.wittypuppy.backend.mail.dto.EmployeeDTO;
import com.wittypuppy.backend.mail.entity.Email;
import com.wittypuppy.backend.mail.entity.EmailAttachment;
import com.wittypuppy.backend.mail.entity.Employee;
import com.wittypuppy.backend.mail.repository.MailEmailAttachmentRepository;
import com.wittypuppy.backend.mail.repository.MailEmailRepository;
import com.wittypuppy.backend.mail.repository.MailEmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmailService {
    private final ModelMapper modelMapper;
    private final MailEmailRepository emailRepository;
    private final MailEmployeeRepository employeeRepository;
    private final MailEmailAttachmentRepository attachmentRepository;

    public EmailService(ModelMapper modelMapper, MailEmailRepository emailRepository, MailEmployeeRepository employeeRepository, MailEmailAttachmentRepository attachmentRepository) {
        this.modelMapper = modelMapper;
        this.emailRepository = emailRepository;
        this.employeeRepository = employeeRepository;
        this.attachmentRepository = attachmentRepository;
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

        Employee employee = employeeRepository.findByEmployeeIdLike(emailReceiverEmployee);

        return modelMapper.map(employee,EmployeeDTO.class);
    }

    @Transactional
    public EmailDTO sendMail(EmailDTO email, String status){
        //여기에 상태에 맞춰서 처리하자.
        if (status.equals("temporary")) {   //임시저장
            email.setEmailStatus("temporary");//별도 처리 필요 없음
        }
        Email emailEntity = modelMapper.map(email,Email.class);    //엔티티로 변환 됨
        emailEntity.setEmailSendTime(LocalDateTime.now());
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
    public List<EmailDTO> findByEmailReceiver(String word){
        Employee employee = new Employee();
        employee.setEmployeeId(word);
        return convert(emailRepository.findAllByEmailSender(employee),EmailDTO.class);
    }
    public List<EmailDTO> findByEmailSendTime(String word) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(word, formatter);
        System.out.println(dateTime);
        return convert(emailRepository.findAllByEmailSendTime(dateTime),EmailDTO.class);
    }
    public List<EmailDTO> findByEmailContent(String word) {
        return convert(emailRepository.findAllByEmailContentLike(word),EmailDTO.class);
    }
    public Page<EmailDTO> findReceiveMail(String emailStatus, User user, Pageable pageable) {
        if(emailStatus.equals("send")) { //클라이언트에서 send를 가져왔으면
            Employee employee = new Employee();
            employee.setEmployeeCode((long) user.getEmployeeCode());    //유저의 코드 삽입
            System.out.println("이메일 조회할 때 나의 정보는 : " + employee.getEmployeeCode());
            Page<Email> emailList = emailRepository.findByEmailReceiverAndEmailStatusInOrderByEmailSendTimeDesc(employee, List.of("send", "important"), pageable);  //send,important인 메일을 찾아라
            System.out.println("겟 토탈 엘리먼트 : " + emailList.getTotalElements());
            System.out.println("겟 토탈 페이지 : " + emailList.getTotalPages());
            System.out.println("겟 컨텐츠 : " + emailList.getContent());
            System.out.println("겟 컨텐츠 : " + emailList.getPageable());
            return  emailList.map(email -> modelMapper.map(email,EmailDTO.class));
        } else if (emailStatus.equals("me")){
            Employee employee = new Employee();
            employee.setEmployeeCode((long) user.getEmployeeCode());
            Page<Email> emailList = emailRepository.findAllByEmailSenderOrderByEmailSendTimeDesc(employee,pageable);
            return emailList.map(email -> modelMapper.map(email,EmailDTO.class));
        } else {    //클라에서 send말고 다른걸 가져왔으면
            Employee employee = new Employee();
            employee.setEmployeeCode((long) user.getEmployeeCode());
            Page<Email> emailList = emailRepository.findAllByEmailReceiverAndEmailStatusOrderByEmailSendTimeDesc(employee, emailStatus, pageable);   //가져온 상태로 찾아라
            System.out.println("요청한 emailStatus는 : " + emailStatus + "입니다.");
            return emailList.map(email -> modelMapper.map(email,EmailDTO.class));
        }

    }
    public Page<EmailDTO> findByEmailTitle(String word,EmployeeDTO me,Pageable pageable) {
        Page<Email> emails = emailRepository.findAllByEmailTitleContainingAndEmailReceiver(word,modelMapper.map(me,Employee.class),pageable);
        return emails.map(email -> modelMapper.map(email,EmailDTO.class));
    }
    public Page<EmailDTO> findAllByEmailSender(String word, Long me,Pageable pageable) {
        //얘가 찾아오는 것은 Employee테이블에서 word변수의 아이디를 갖고 있는 사람을 가져온다.
            List<Employee> employee = employeeRepository.findAllByEmployeeIdLike("%" + word + "%");
            if(employee.isEmpty()){
                System.out.println("유저를 못 찾음");
                return null;
            }
            List<Long> emailSenderId = new ArrayList<>();
            for(Employee emp : employee){
                emailSenderId.add(emp.getEmployeeCode());
            }
            System.out.println("이메일 보낸 사람의 코드는? : "+emailSenderId); //이게 확인 용이다.
            //me는 나인 15이고, 두 번째 인자는 2,9,16이다.
            Page<Email> emails = emailRepository.findAllByEmailReceiverMail(me,emailSenderId,pageable);
            if(!emails.isEmpty()) {
                System.out.println("DB에서 가져온 값은?");
                for (Email email : emails) {
                    System.out.println(email);
                }
                return emails.map(email -> modelMapper.map(email,EmailDTO.class));
            }
            System.out.println(emails);
            return null;
    }
    //예약상태의 메일을 추가하는 메서드
    public EmailDTO sendReserveMail(EmailDTO emailDTO) {
        emailDTO.setEmailStatus("reserve");
        Email email = modelMapper.map(emailDTO,Email.class);
        email.setEmailSendTime(LocalDateTime.now());
        emailRepository.save(email);
        return modelMapper.map(email,EmailDTO.class);
    }
    //예약된 시간에 메일의 상태를 send로 바꿔주는 메서드 알람도 같이 보내자
    public EmailDTO sendReserveMail(Long emailCode) {
        Email email = emailRepository.findById(emailCode)
                .orElseThrow(() -> new EntityNotFoundException(emailCode+"번 이메일을 찾을 수 없습니다."));
        email.setEmailStatus("send");
        emailRepository.save(email);

        return modelMapper.map(email,EmailDTO.class);
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




    public EmailDTO updateEmailReadStatus(EmailDTO emailDTO) {
        Email email = modelMapper.map(emailDTO,Email.class);
        email.setEmailSendTime(LocalDateTime.parse(emailDTO.getEmailSendTime()));
        System.out.println("======================================="+email.getEmailSendTime());
        emailRepository.save(email);
        return modelMapper.map(email,EmailDTO.class);
    }

    public Page<EmailDTO> findByEmailReadStatus(EmployeeDTO user, Pageable pageable) {
        Page<Email> email = emailRepository.findAllByEmailReadStatusAndEmailReceiverAndEmailStatusIn("N",modelMapper.map(user,Employee.class),List.of("send","important"),pageable);
//        for(int i=0; i<email.getSize()-1; i++){
//            System.out.println(email.getContent().get(i).getEmailTitle());
//        }
        Page<EmailDTO> emailDTOPage = email.map(mail -> modelMapper.map(mail,EmailDTO.class));
//        for(int i=0; i<emailDTOPage.getSize()-1; i++){
//            System.out.println(email.getContent().get(i).getEmailTitle());
//        }
        return emailDTOPage;
    }


    public EmailDTO updateStatus(EmailDTO emailDTO, String emailStatus) {
        emailDTO.setEmailStatus(emailStatus);
        Email email = modelMapper.map(emailDTO, Email.class);
        email.setEmailSendTime(LocalDateTime.parse(emailDTO.getEmailSendTime(),DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        Email result = emailRepository.save(email);
        modelMapper.map(result,EmailDTO.class);
        return modelMapper.map(result, EmailDTO.class);
    }


    public Long findByEmailReadStatusCount(User user) {
        Employee userCode = new Employee();
        userCode.setEmployeeCode((long) user.getEmployeeCode());
        return emailRepository.countByEmailReadStatusAndEmailReceiver("N",userCode);


    }

    public boolean updateEmailStatus(Long emailCode, String status) {
        Optional<Email> emailOptional = emailRepository.findById(emailCode);
        if(emailOptional.isPresent()){
            Email email = emailOptional.get();
            email.setEmailStatus(status);
            emailRepository.save(email);
            return true;
        } else {
            return false;
        }

    }

    public EmployeeDTO findByEmployeeId(String user) {
        Employee employee = employeeRepository.findByEmployeeId(user);
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public Page<EmailDTO> findAllByEmailSenderAndEmailReceiver(User user, Pageable pageable) {
        Employee sender = new Employee();
        Employee receiver = new Employee();
        sender.setEmployeeCode((long)user.getEmployeeCode());
        receiver.setEmployeeCode((long)user.getEmployeeCode());
        Page<Email> emails = emailRepository.findAllByEmailSenderAndEmailReceiver(sender,receiver,pageable);
        return emails.map(email -> modelMapper.map(email,EmailDTO.class));
    }

    public void insertMailAttachment(List<EmailAttachmentDTO> attachmentDTOS) {
        List<EmailAttachment> attachmentEntity = convert(attachmentDTOS, EmailAttachment.class);
        attachmentRepository.saveAll(attachmentEntity);
    }

    public EmailDTO findById(Long emailCode) {

        return modelMapper.map(emailRepository.findById(emailCode),EmailDTO.class);
    }

    public List<EmailAttachmentDTO> findAllByEmailCode(EmailDTO email) {
        Email emailEntity = modelMapper.map(email, Email.class);
        List<EmailAttachment> attachmentList = attachmentRepository.findByEmailCode(emailEntity);
        for(EmailAttachment a : attachmentList){
            System.out.println(a.getEmailCode());
            System.out.println(a.getAttachmentOgFile());
        }
        return convert(attachmentList,EmailAttachmentDTO.class);
    }

    public EmailAttachment getFileById(Long attachmentCode) {
        return attachmentRepository.findById(attachmentCode).orElseThrow(null);
    }
}
