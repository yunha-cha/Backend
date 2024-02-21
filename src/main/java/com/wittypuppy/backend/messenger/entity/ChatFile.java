package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
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
    private Date chatFileUpdateDate;

    public ChatFile setChatFileCode(Long chatFileCode) {
        this.chatFileCode = chatFileCode;
        return this;
    }

    public ChatFile setChatCode(Long chatCode) {
        this.chatCode = chatCode;
        return this;
    }

    public ChatFile setChatFileOgFile(String chatFileOgFile) {
        this.chatFileOgFile = chatFileOgFile;
        return this;
    }

    public ChatFile setChatFileChangedFile(String chatFileChangedFile) {
        this.chatFileChangedFile = chatFileChangedFile;
        return this;
    }

    public ChatFile setChatFileUpdateDate(Date chatFileUpdateDate) {
        this.chatFileUpdateDate = chatFileUpdateDate;
        return this;
    }

    public ChatFile builder() {
        return new ChatFile(chatFileCode, chatCode, chatFileOgFile, chatFileChangedFile, chatFileUpdateDate);
    }
}
