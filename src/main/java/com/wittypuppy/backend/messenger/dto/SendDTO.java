package com.wittypuppy.backend.messenger.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SendDTO {
    private Integer employeeCode; // 누가 보냈는지
    private String chatContent; // 내용은 뭔지
    private String chatFileName;
    private String chatFile;
    private String chatFileURL;
    private String isFileSend;
}
