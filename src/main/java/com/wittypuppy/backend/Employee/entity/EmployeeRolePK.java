package com.wittypuppy.backend.Employee.entity;

import lombok.*;

import java.io.Serializable;

/* 복합키 타입을 정의할 때는 Serializable을 반드시 구현 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeRolePK implements Serializable {

    private int employeeCode;
    private int authorityCode;

    // equals와 hashCode 메서드 구현

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeRolePK that = (EmployeeRolePK) o;

        if (employeeCode != that.employeeCode) return false;
        return authorityCode == that.authorityCode;
    }

    @Override
    public int hashCode() {
        int result = employeeCode;
        result = 31 * result + authorityCode;
        return result;
    }



}
