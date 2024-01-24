package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_post_attachment")
public class PostAttachment {
    @Id
    @Column(name = "post_attachment_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postAttachmentCode;

    @Column(name = "post_code", columnDefinition = "BIGINT")
    private Long postCode;

    @Column(name = "post_attachment_og_file", columnDefinition = "VARCHAR(500)")
    private String postAttachmentOgFile;

    @Column(name = "post_attachment_changed_file", columnDefinition = "VARCHAR(500)")
    private String postAttachmentChangedFile;

    @Column(name = "post_attachment_date", columnDefinition = "BIGINT")
    private LocalDateTime postAttachmentDate;

    @Column(name = "post_delete_status", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String postDeleteStatus;
}
