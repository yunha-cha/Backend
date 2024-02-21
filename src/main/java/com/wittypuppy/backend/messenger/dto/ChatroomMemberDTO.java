package com.wittypuppy.backend.messenger.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ChatroomMemberDTO {
    private Long chatroomMemberCode;
    private Long chatroomCode;
    private EmployeeDTO employee;
    private String chatroomMemberType;
    private String chatroomMemberPinnedStatus;
}
