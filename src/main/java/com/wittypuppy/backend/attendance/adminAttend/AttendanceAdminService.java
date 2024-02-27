package com.wittypuppy.backend.attendance.adminAttend;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.attendance.dto.VacationDTO;
import com.wittypuppy.backend.attendance.paging.Criteria;
import com.wittypuppy.backend.attendance.repository.AttendanceAdminEmployee;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Service
@Slf4j
public class AttendanceAdminService {


    private final AttendanceAdminEmployee attendanceAdminEmployee;
    private final ModelMapper modelMapper;

    public AttendanceAdminService(AttendanceAdminEmployee attendanceAdminEmployee, ModelMapper modelMapper) {
        this.attendanceAdminEmployee = attendanceAdminEmployee;
        this.modelMapper = modelMapper;
    }

    public Page<AdminEmployeeDTO> mainVacation(Criteria cri) {

        System.out.println("=====service=====mainVacation tStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.unsorted());


        Page<AdminEmployee> result = attendanceAdminEmployee.findEmp(paging);

        Page<AdminEmployeeDTO> resultList = result.map(adminVacation -> modelMapper.map(adminVacation, AdminEmployeeDTO.class));


        System.out.println("========resultList======= " + resultList);
        System.out.println("======== mainVacation end ============");

        return resultList;

    }

    public Page<AdminEmployeeDTO> noVacation(Criteria cri) {

        System.out.println("=====service=====noVacation tStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.unsorted());


        Page<AdminEmployee> result = attendanceAdminEmployee.findNo(paging);

        Page<AdminEmployeeDTO> resultList = result.map(adminNoVacation -> modelMapper.map(adminNoVacation, AdminEmployeeDTO.class));


        System.out.println("========resultList======= " + resultList);
        System.out.println("======== noVacation end ============");

        return resultList;

    }


    @Transactional
    public VacationDTO insertVacation(User employeeCode) {


        // 1년이상 근무자 중 연차 생성 안된 직원 조회
        List<AdminEmployee> yearOver = attendanceAdminEmployee.noYearVacation();
        System.out.println(" 1년이상 근무자 목록 ===== yearOver ==== " + yearOver);

        //1년 미만 근무자 중 연자 생성 안된 직원 조회
        List<AdminEmployee> yearUnder = attendanceAdminEmployee.noUnderVacation();
        System.out.println("1년 미만 근무자 목록 yearUnder = " + yearUnder);

        //셍성일 null 이거나 첫 한달 만근한 직원 조회
        List<AdminEmployee> first = attendanceAdminEmployee.firstVacation();
        System.out.println("첫 한달 만근한 직원 && 생성일이 null 인거 :=======first = " + first);

        //연차 생성할 목록 모두 조회
        List<AdminEmployee> all = attendanceAdminEmployee.all();
        System.out.println("모든 인서트할 직원 조회" + all);


        // 현재 날짜 구하기
        LocalDate currentDate = LocalDate.now();


        if (all.isEmpty()) {
            System.out.println("대상이 없습니다");
        } else {

            //생성되는 연차수량
            int vacationDays = 0;

            //생성이유
            String reason = null;

            //생성일
            LocalDateTime create = null;

            //만기일
            LocalDateTime done = null;

            for (AdminEmployee employee : all) {
                LocalDateTime joinDate = employee.getEmployeeJoinDateAdmin();  // employee의 입사일부터 경과한 일수 계산
                LocalDateTime createDate = employee.getVacationCreationDate();
                int emp = employee.getAdminEmployeeCode();
                long daysSinceJoin = Duration.between(joinDate, LocalDateTime.now()).toDays(); // 입사일부터 현재까지 경과한 일수 계산
                long yearsSinceJoin = daysSinceJoin / 365; // 현재까지 경과한 연 수
                if (yearsSinceJoin == 1) {
                    // 1년째인 경우는 연차를 15일로 설정
                    vacationDays = 15;
                    reason = "일년만근";
                    System.out.println("====== 1 ==========");
                    create =  LocalDate.of(currentDate.getYear(), Month.JANUARY, 1).atStartOfDay();// 매년 1월1일
                    done = LocalDate.of(currentDate.getYear() + 1, Month.MARCH, 1).atStartOfDay().minusDays(1);

                    } else {
                        // 1년 이상 경과 후부터는 2년에 1개씩 증가
                        vacationDays = 15 + (int) ((yearsSinceJoin - 1) / 2);
                        System.out.println("========== 2 ========");
                        reason = "일년만근";
                        create =  LocalDate.of(currentDate.getYear(), Month.JANUARY, 1).atStartOfDay();// 매년 1월1일
                        done = LocalDate.of(currentDate.getYear() + 1, Month.MARCH, 1).atStartOfDay().minusDays(1);
                    }


                if(createDate == null) {
                    if (Duration.between(employee.getEmployeeJoinDateAdmin(), LocalDateTime.now()).toDays() < 365) {
                        vacationDays = 1;
                        reason = "한달만근";
                        create = employee.getEmployeeJoinDateAdmin().plusMonths(1); // 입사일로부터 한 달 후
                        done = LocalDate.of(currentDate.getYear() + 1, Month.MARCH, 1).atStartOfDay().minusDays(1);
                        System.out.println(" ======= 첫 한달 만근");
                        System.out.println("========== 3 ========");
                    }

                }

                if(createDate != null){
                    if (Duration.between(employee.getEmployeeJoinDateAdmin(), LocalDateTime.now()).toDays() < 365) {
                        vacationDays = 1;
                        reason = "한달만근";
                        create = employee.getVacationCreationDate().plusMonths(1); // 첫 생성일 부터 한 달 후
                        done = LocalDate.of(currentDate.getYear() + 1, Month.MARCH, 1).atStartOfDay().minusDays(1);
                        System.out.println(" ======= 첫 이후 만근 =====");
                    }
                }

                }

            System.out.println("연차 일수: " + vacationDays);
            System.out.println("생성이유 : " + reason);
            System.out.println("생성일 :" + create);
            System.out.println("만기일 : " + done);


        }
        return null;
    }



}
