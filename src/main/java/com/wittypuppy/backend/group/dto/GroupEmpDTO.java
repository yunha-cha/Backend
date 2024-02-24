package com.wittypuppy.backend.group.dto;
import lombok.*;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        @ToString

        public class GroupEmpDTO {


        private Long empCode;

        private GroupDeptDTO department;

        private String empName;

        private String empEmail;

        private String phone;

        private GroupJobDTO job;


}

