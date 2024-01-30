package com.wittypuppy.backend.mail.dto;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
public class EmailDTO {
    private Long emailCode;
    private String emailReadStatus;
    private String emailTitle;
    private String emailContent;
    private LocalDateTime emailSendTime;
    private LocalDateTime emailReservationTime;
    private String emailStatus;

    private EmployeeDTO emailSender;
    private EmployeeDTO emailReceiver;
}
