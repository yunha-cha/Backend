package com.wittypuppy.backend.messenger.dto;

import java.util.Date;

public interface ChatroomMessengerMainInterface {
    Long getChatroomCode();

    String getChatroomTitle();

    String getChatroomFixedStatus();

    String getChatroomContent();

    Date getChatroomChatDate();

    String getChatroomProfileFileURL();

    Long getChatroomMemberCount();

    Long getNotReadChatCount();
}
