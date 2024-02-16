package com.wittypuppy.backend.group.dto;

import com.wittypuppy.backend.group.entity.GroupEmp;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GroupJobDTO {

    private Long jobCode;

    private String jobName;

//    private List<GroupEmp> employeeList;


}
