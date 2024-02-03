package com.wittypuppy.backend.messenger.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ChatroomOptionsDTO {
    private Long chatroomCode;
    private String chatroomTitle;
    private String chatroomFixedStatus;

    public ChatroomOptionsDTO setChatroomCode(Long chatroomCode) {
        this.chatroomCode = chatroomCode;
        return this;
    }

    public ChatroomOptionsDTO setChatroomTitle(String chatroomTitle) {
        this.chatroomTitle = chatroomTitle;
        return this;
    }

    public ChatroomOptionsDTO setChatroomFixedStatus(String chatroomFixedStatus) {
        this.chatroomFixedStatus = chatroomFixedStatus;
        return this;
    }

    public ChatroomOptionsDTO builder() {
        return new ChatroomOptionsDTO(chatroomCode, chatroomTitle, chatroomFixedStatus);
    }
}
