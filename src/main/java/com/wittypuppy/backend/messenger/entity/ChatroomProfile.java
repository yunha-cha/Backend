package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity(name = "MESSENGER_CHATROOM_PROFILE")
@Table(name = "tbl_chatroom_profile")
public class ChatroomProfile {

    @Id
    @Column(name = "chatroom_profile_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomProfileCode;

    @Column(name = "chatroom_code")
    private Long chatroomCode;

    @Column(name = "chatroom_profile_og_file")
    private String chatroomProfileOgFile;

    @Column(name = "chatroom_profile_changed_file")
    private String chatroomProfileChangedFile;

    @Column(name = "chatroom_profile_regist_date")
    private Date chatroomProfileRegistDate;

    @Column(name = "chatroom_profile_delete_status")
    private String chatroomProfileDeleteStatus;

    public ChatroomProfile setChatroomProfileCode(Long chatroomProfileCode) {
        this.chatroomProfileCode = chatroomProfileCode;
        return this;
    }

    public ChatroomProfile setChatroomCode(Long chatroomCode) {
        this.chatroomCode = chatroomCode;
        return this;
    }

    public ChatroomProfile setChatroomProfileOgFile(String chatroomProfileOgFile) {
        this.chatroomProfileOgFile = chatroomProfileOgFile;
        return this;
    }

    public ChatroomProfile setChatroomProfileChangedFile(String chatroomProfileChangedFile) {
        this.chatroomProfileChangedFile = chatroomProfileChangedFile;
        return this;
    }

    public ChatroomProfile setChatroomProfileRegistDate(Date chatroomProfileRegistDate) {
        this.chatroomProfileRegistDate = chatroomProfileRegistDate;
        return this;
    }

    public ChatroomProfile setChatroomProfileDeleteStatus(String chatroomProfileDeleteStatus) {
        this.chatroomProfileDeleteStatus = chatroomProfileDeleteStatus;
        return this;
    }

    public ChatroomProfile builder() {
        return new ChatroomProfile(chatroomProfileCode, chatroomCode, chatroomProfileOgFile, chatroomProfileChangedFile, chatroomProfileRegistDate, chatroomProfileDeleteStatus);
    }
}
