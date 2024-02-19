package com.wittypuppy.backend.messenger.dto;

import lombok.*;

import java.util.Date;


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
    private Date chatroomChatDate;
    private String chatroomProfileFileURL;
    private Long chatroomMemberCount;
    private Long notReadChatCount;
}
