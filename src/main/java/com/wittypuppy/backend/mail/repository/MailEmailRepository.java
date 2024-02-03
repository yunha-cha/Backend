package com.wittypuppy.backend.mail.repository;

import com.wittypuppy.backend.mail.entity.Email;
import com.wittypuppy.backend.mail.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MailEmailRepository extends JpaRepository<Email,Long> {

//    @Query("SELECT e FROM Email e WHERE e.emailStatus = :emailStatus AND e.emailReceiverEmployee = :userCode")
//    List<EmailDTO> findReceiveMail(
//            @Param("userCode")Employee userCode,
//            @Param("emailStatus") String emailStatus
//    );
    @Query("SELECT e FROM MAIL_EMAIL e JOIN e.emailReceiver ere WHERE e.emailStatus = :emailStatus AND ere.employeeCode = :userCode")
    List<Email> findReceiveMail(
            @Param("userCode")Long userCode,
            @Param("emailStatus") String emailStatus
    );
    @Query("SELECT e FROM MAIL_EMAIL e JOIN e.emailSender ere WHERE e.emailStatus = :emailStatus AND ere.employeeCode = :userCode")
    List<Email> findSendMail(
            @Param("userCode")Long userCode,
            @Param("emailStatus") String emailStatus
    );
    List<Email> findByEmailCodeIn(List<Long> emailEntity);
    List<Email> findByEmailSenderAndEmailStatus(Employee employeeCode, String emailStatus);
    List<Email> findAllByEmailSender(Employee employee);
    List<Email> findAllByEmailSendTime(LocalDateTime word);
    List<Email> findAllByEmailContentLike(String word);
    List<Object> findAllByEmailTitleLike(String word);
}
