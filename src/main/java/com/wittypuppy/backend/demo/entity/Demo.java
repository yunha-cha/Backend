package com.wittypuppy.backend.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_demo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Demo {

    @Id
    @Column(name = "demo_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long demoCode;

    @Column(name = "demo_column_1")
    private String column1;

    @Column(name = "demo_column_2")
    private Long column2;

    @Column(name = "demo_column_3")
    private Double column3;

    @Column(name = "demo_column_4")
    private LocalDateTime column4;


    public Demo demoCode(Long demoCode) {
        this.demoCode = demoCode;
        return this;
    }

    public Demo column1(String column1) {
        this.column1 = column1;
        return this;
    }

    public Demo column2(Long column2) {
        this.column2 = column2;
        return this;
    }

    public Demo column3(Double column3) {
        this.column3 = column3;
        return this;
    }

    public Demo column4(LocalDateTime column4) {
        this.column4 = column4;
        return this;
    }

    public Demo build() {
        return new Demo(demoCode, column1, column2, column3, column4);
    }
}
