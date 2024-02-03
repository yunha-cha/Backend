package com.wittypuppy.backend.messenger.dto;

import com.wittypuppy.backend.project.entity.Employee;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ChatroomMemberDTO {
    private Long chatroomMemberCode;
    private Long chatroomCode;
    private Employee employee;
    private String chatroomMemberType;
    private String chatroomMemberPinnedStatus;
}
