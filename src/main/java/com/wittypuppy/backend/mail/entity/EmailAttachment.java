package com.wittypuppy.backend.mail.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Setter@Getter
@Entity(name = "MAIL_ATTACHMENT")
@Table(name = "tbl_email_attachment")
public class EmailAttachment {
    @Id
    @Column(name = "email_attachment_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentCode;
    @Column(name = "email_attachment_date")
    private Date attachmentDate;
    @Column(name ="email_attachment_changed_file")
    private String attachmentChangedFile;
    @Column(name ="email_attachment_og_file")
    private String attachmentOgFile;
    @Column(name ="email_attachment_delete_status")
    private String attachmentDeleteStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_code")
    private Email emailCode;
}
