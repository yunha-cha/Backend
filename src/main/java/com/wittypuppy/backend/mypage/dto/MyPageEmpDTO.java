package com.wittypuppy.backend.mypage.dto;
import com.wittypuppy.backend.group.dto.GroupDeptDTO;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class MyPageEmpDTO {


private Long empCode;

private GroupDeptDTO department;

private String empName;

private String empEmail;

private String phone;

private Date empBirth;


}

