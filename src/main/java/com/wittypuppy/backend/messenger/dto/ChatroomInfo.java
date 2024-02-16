package com.wittypuppy.backend.messenger.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatroomInfo {
    private List<Long> chatroomCodeList;
    private String isRemainingChat;
}
