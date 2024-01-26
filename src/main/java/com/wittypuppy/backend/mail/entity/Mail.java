package com.wittypuppy.backend.mail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
@Table(name="tbl_email")
public class Mail {
    @Id
    @Column(name="email_code")
    private Integer mailCode;

}
