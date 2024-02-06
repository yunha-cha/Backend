package com.wittypuppy.backend.messenger.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ChatroomMainDTO {
    private Long chatroomCode;
    private String chatroomTitle;
    private String chatroomProfileFileURL;
    private Long lastReadChatCode;
    private String pinnedStatus;
    private List<ChatroomMemberDTO> chatroomMemberDTOList;
    private List<ChatDTO> chatDTOList;

    public ChatroomMainDTO setChatroomCode(Long chatroomCode) {
        this.chatroomCode = chatroomCode;
        return this;
    }

    public ChatroomMainDTO setChatroomTitle(String chatroomTitle) {
        this.chatroomTitle = chatroomTitle;
        return this;
    }

    public ChatroomMainDTO setChatroomProfileFileURL(String chatroomProfileFileURL) {
        this.chatroomProfileFileURL = chatroomProfileFileURL;
        return this;
    }

    public ChatroomMainDTO setLastReadChatCode(Long lastReadChatCode) {
        this.lastReadChatCode = lastReadChatCode;
        return this;
    }

    public ChatroomMainDTO setPinnedStatus(String pinnedStatus) {
        this.pinnedStatus = pinnedStatus;
        return this;
    }

    public ChatroomMainDTO setChatroomMemberDTOList(List<ChatroomMemberDTO> chatroomMemberDTOList) {
        this.chatroomMemberDTOList = chatroomMemberDTOList;
        return this;
    }

    public ChatroomMainDTO setChatDTOList(List<ChatDTO> chatDTOList) {
        this.chatDTOList = chatDTOList;
        return this;
    }

    public ChatroomMainDTO builder() {
        return new ChatroomMainDTO(chatroomCode, chatroomTitle, chatroomProfileFileURL, lastReadChatCode, pinnedStatus, chatroomMemberDTOList, chatDTOList);
    }
}
