package com.wittypuppy.backend.messenger.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SendDTO {
    private Long chatroomMemberCode; // 누가 보냈는지
    private Date chatWriteDate; // 언제 보냈는지
    private String chatContent; // 내용은 뭔지
    private List<MultipartFile> chatFileList; // 만약 사진이 있다면 사진은?
}
