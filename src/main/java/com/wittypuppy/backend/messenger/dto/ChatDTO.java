package com.wittypuppy.backend.messenger.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ChatDTO {
    private Long chatCode;
    private Long chatroomCode;
    private Long chatroomMemberCode;
    private LocalDateTime chatWriteDate;
    private String chatContent;
    private List<ChatFileDTO> chatFileList;
    private ChatroomMemberDTO chatroomMember;
}
