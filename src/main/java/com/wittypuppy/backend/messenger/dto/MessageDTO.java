package com.wittypuppy.backend.messenger.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MessageDTO {
    private String from;
    private String text;
}
