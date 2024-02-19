package com.wittypuppy.backend.messenger.dto;

public interface chatroomMainElseChatInterface {
    Long getChatroomCode();

    String getChatroomTitle();

    String getChatroomProfileFileURL();

    Long getLastReadChatCode();

    String getPinnedStatus();
}
