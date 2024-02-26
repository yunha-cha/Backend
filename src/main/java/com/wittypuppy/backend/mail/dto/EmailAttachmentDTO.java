package com.wittypuppy.backend.mail.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
public class EmailAttachmentDTO {
    private Long attachmentCode;
    private Date attachmentDate;
    private String attachmentChangedFile;
    private String attachmentOgFile;
    private String attachmentDeleteStatus;
    private EmailDTO emailCode;
}
