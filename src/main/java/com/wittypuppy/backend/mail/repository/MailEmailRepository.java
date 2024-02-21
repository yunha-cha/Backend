package com.wittypuppy.backend.mail.repository;

import com.wittypuppy.backend.mail.entity.Email;
import com.wittypuppy.backend.mail.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MailEmailRepository extends JpaRepository<Email,Long> {

//    @Query("SELECT e FROM Email e WHERE e.emailStatus = :emailStatus AND e.emailReceiverEmployee = :userCode")
//    List<EmailDTO> findReceiveMail(
//            @Param("userCode")EmployeeDTO userCode,
//            @Param("emailStatus") String emailStatus
//    );
    @Query("SELECT e FROM MAIL_EMAIL e JOIN e.emailReceiver ere WHERE e.emailStatus = :emailStatus AND ere.employeeCode = :userCode")
    List<Email> findReceiveMail(
            @Param("userCode")Long userCode,
            @Param("emailStatus") String emailStatus
    );
    @Query("SELECT e FROM MAIL_EMAIL e JOIN e.emailSender ere WHERE e.emailStatus = :emailStatus AND ere.employeeCode = :userCode ORDER BY e.emailSendTime asc")
    List<Email> findSendMail(
            @Param("userCode")Long userCode,
            @Param("emailStatus") String emailStatus
    );
    List<Email> findByEmailCodeIn(List<Long> emailEntity);
    List<Email> findByEmailSenderAndEmailStatus(Employee employeeCode, String emailStatus);
    List<Email> findAllByEmailSender(Employee employee);
    List<Email> findAllByEmailSendTime(LocalDateTime word);
    List<Email> findAllByEmailContentLike(String word);

    Page<Email> findAllByEmailTitleContainingAndEmailReceiver(String word, Employee me, Pageable pageable);
    @Query(nativeQuery = true,
            value = "SELECT * FROM tbl_email WHERE email_receiver_employee_code = :receiver AND email_sender_employee_code IN :sender ORDER BY email_send_time desc")
    Page<Email> findAllByEmailReceiverMail(@Param("receiver") Long receiver, @Param("sender") List<Long> sender, Pageable pageable);

    List<Email> findByEmailReceiverAndEmailStatusIn(Employee employee, List<String> strings);


    Long countByEmailReadStatusAndEmailReceiver(String readStatus, Employee employee);

    Page<Email> findAllByEmailReadStatusAndEmailReceiverAndEmailStatusIn(String n, Employee map, List<String> send, Pageable pageable);

    Page<Email> findByEmailReceiverAndEmailStatusInOrderByEmailSendTimeDesc(Employee employee, List<String> send, Pageable pageable);

    Page<Email> findAllByEmailSenderOrderByEmailSendTimeDesc(Employee employee,Pageable pageable);

    Page<Email> findAllByEmailReceiverAndEmailStatusOrderByEmailSendTimeDesc(Employee employeeCode, String emailStatus, Pageable pageable);

    Page<Email> findAllByEmailSenderAndEmailReceiver(Employee sender, Employee receiver,Pageable pageable);
}
