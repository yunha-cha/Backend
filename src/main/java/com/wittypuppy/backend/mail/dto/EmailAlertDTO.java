package com.wittypuppy.backend.mail.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter@Getter@ToString
public class EmailAlertDTO {
    private Long emailCode;
    private String sender;
    private String receiver;
    private Date sendTime;
    private String detail;
}
