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
@Table(name = "tbl_chat_file")
public class ChatFile {
    @Id
    @Column(name = "chat_file_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatFileCode;

    @Column(name = "chat_code", columnDefinition = "BIGINT")
    private Long chatCode;

    @Column(name = "chat_file_og_file", columnDefinition = "VARCHAR(500)")
    private String chatFileOgFile;

    @Column(name = "chat_file_changed_file", columnDefinition = "VARCHAR(500)")
    private String chatFileChangedFile;

    @Column(name = "upload_date", columnDefinition = "DATETIME")
    private LocalDateTime uploadDate;
}
