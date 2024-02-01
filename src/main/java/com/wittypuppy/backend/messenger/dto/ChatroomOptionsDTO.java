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
public class ChatroomOptionsDTO {
    private String chatroomTitle;
    private List<Long> employeeCodeList;
}
