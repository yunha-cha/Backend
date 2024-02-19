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
    private List<ChatroomMemberDTO> chatroomMemberList;
    private List<ChatDTO> chatList;
    private Long recentPageNum;
    private Long recentChatCode;
    private Long recentPageChatCount;

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

    public ChatroomMainDTO setChatroomMemberDTOList(List<ChatroomMemberDTO> chatroomMemberList) {
        this.chatroomMemberList = chatroomMemberList;
        return this;
    }

    public ChatroomMainDTO setChatDTOList(List<ChatDTO> chatList) {
        this.chatList = chatList;
        return this;
    }

    public ChatroomMainDTO setRecentPageNum(Long recentPageNum) {
        this.recentPageNum = recentPageNum;
        return this;
    }

    public ChatroomMainDTO setRecentChatCode(Long recentChatCode) {
        this.recentChatCode = recentChatCode;
        return this;
    }

    public ChatroomMainDTO setRecentPageChatCount(Long recentPageChatCount) {
        this.recentPageChatCount = recentPageChatCount;
        return this;
    }

    public ChatroomMainDTO builder() {
        return new ChatroomMainDTO(chatroomCode, chatroomTitle, chatroomProfileFileURL, lastReadChatCode, pinnedStatus, chatroomMemberList, chatList, recentPageNum, recentChatCode, recentPageChatCount);
    }
}
