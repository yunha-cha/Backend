package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "MESSENGER_CHAT_FILE")
@Table(name = "tbl_chat_file")
public class ChatFile {
    @Id
    @Column(name = "chat_file_code")
    private Long chatFileCode;

    @Column(name = "chat_code")
    private Long chatCode;

    @Column(name = "chat_file_og_file")
    private String chatFileOgFile;

    @Column(name = "chat_file_changed_file")
    private String chatFileChangedFile;

    @Column(name = "upload_date")
    private LocalDateTime chatroomFixedStatus;
}
