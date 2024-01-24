package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.beans.Encoder;
import java.nio.charset.Charset;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_auth")
public class Auth {
    @Id
    @Column(name = "auth_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "auth_number", columnDefinition = "BIGINT")
    private Long authNumber;
}
