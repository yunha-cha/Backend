package com.wittypuppy.backend.messenger.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ChatDTO {
    private Long chatCode;
    private Long chatroomCode;
    private ChatroomMemberDTO chatroomMember;
    private Date chatWriteDate;
    private String chatContent;
    private List<ChatFileDTO> chatFileList;
}
