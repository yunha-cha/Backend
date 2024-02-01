package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "MESSENGER_CHATROOM_PROFILE")
@Table(name = "tbl_chatroom_profile")
public class ChatroomProfile {
    @Id
    @Column(name = "chatroom_profile_code")
    private Long chatroomProfileCode;

    @Column(name = "chatroom_code")
    private Long chatroomCode;

    @Column(name = "chatroom_profile_og_file")
    private String chatroomProfileOgFile;

    @Column(name = "chatroom_profile_changed_file")
    private String chatroomProfileChangedFile;

    @Column(name = "chatroom_profile_regist_date")
    private LocalDateTime chatroomProfileRegistDate;

    @Column(name = "chatroom_profile_delete_status")
    private String chatroomProfileDeleteStatus;
}
