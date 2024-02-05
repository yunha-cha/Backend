package com.wittypuppy.backend.messenger.dto;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ChatroomMainDTO {
    private Long chatroomCode;
    private String chatroomTitle;
    private String chatroomProfileFileURL;
    private List<ChatDTO> chatList;
    private Long lastReadChatCode;
    private Long chatroomMemberCount;
}
