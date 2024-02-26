package com.wittypuppy.backend.mail.repository;

import com.wittypuppy.backend.mail.entity.Email;
import com.wittypuppy.backend.mail.entity.EmailAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailEmailAttachmentRepository extends JpaRepository<EmailAttachment,Long> {

    List<EmailAttachment> findByEmailCode(Email emailCode);
}
