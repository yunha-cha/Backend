package com.wittypuppy.backend.messenger.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatroomMessengerMainDTO {
    private Long chatroomCode;
    private String chatroomTitle;
    private String chatroomFixedStatus;
    private String chatroomContent;
    private LocalDateTime chatroomChatDate;
    private Long notReadChatCount;
    private String chatroomProfileChangedFile;
    private Long chatroomMemberCount;
}
