package com.wittypuppy.backend.mainpage.service;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.attendance.dto.AttendanceManagementDTO;
import com.wittypuppy.backend.attendance.dto.EmployeeDTO;
import com.wittypuppy.backend.attendance.dto.InsertAttendanceManagementDTO;
import com.wittypuppy.backend.attendance.entity.AttendanceManagement;
import com.wittypuppy.backend.attendance.entity.Employee;
import com.wittypuppy.backend.attendance.entity.InsertAttendanceManagement;
import com.wittypuppy.backend.mainpage.dto.MainPageBoardDTO;
import com.wittypuppy.backend.mainpage.dto.MainPageProjectListDTO;
import com.wittypuppy.backend.mainpage.entity.MainPagePost;
import com.wittypuppy.backend.mainpage.entity.MainPageProject;
import com.wittypuppy.backend.mainpage.repository.MainPageAttendanceRepository;
import com.wittypuppy.backend.mainpage.repository.MainPageBoardRepository;
import com.wittypuppy.backend.mainpage.repository.MainPageProjectRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MainPageService {

    private final MainPageBoardRepository mainPageBoardRepository;
    private final MainPageProjectRepository mainPageProjectRepository;

    private final MainPageAttendanceRepository mainPageAttendanceRepository;


    private final ModelMapper modelMapper;

    public MainPageService(MainPageBoardRepository mainPageBoardRepository, MainPageProjectRepository mainPageProjectRepository, MainPageAttendanceRepository mainPageAttendanceRepository, ModelMapper modelMapper) {
        this.mainPageBoardRepository = mainPageBoardRepository;
        this.mainPageProjectRepository = mainPageProjectRepository;
        this.mainPageAttendanceRepository = mainPageAttendanceRepository;
        this.modelMapper = modelMapper;
    }

    public List<MainPageBoardDTO> selectPostList(){
        log.info("메인페이지 게시판 리스트출력 서비스 시작");
        List<MainPagePost> postList = mainPageBoardRepository.findAllByOrderByPostDateDesc();
        List<MainPageBoardDTO> mainPageBoardDTOList = postList.stream()
                .map(post -> modelMapper.map(post, MainPageBoardDTO.class))
                .collect(Collectors.toList());

        System.out.println("mainPageBoardDTOList = " + mainPageBoardDTOList);
        log.info("서비스 게시판 부분 끝");
        return mainPageBoardDTOList;

    }

    public List<MainPageProjectListDTO> selectProjectList(){
        log.info("메인페이지 프로젝트 리스트 서비스 시작");
        List<MainPageProject> projectList = mainPageProjectRepository.findAll();

        // 프로젝트를 생성일자를 기준으로 내림차순으로 정렬
//        projectList.sort(Comparator.comparing(MainPageProject::getProjectPostList)
//                .reversed()
//                .thenComparing(MainPageProject::getProjectCode));

        List<MainPageProjectListDTO> mainPageProjectListDTOList = projectList.stream()
                .map(post -> modelMapper.map(post, MainPageProjectListDTO.class))
                .collect(Collectors.toList());

        System.out.println("mainPageProjectListDTOList = " + mainPageProjectListDTOList);
        log.info("서비스 프로젝트 부분 끝");
        return mainPageProjectListDTOList;
    }

    public AttendanceManagementDTO attendanceList(int employeeCode) {

//        AttendanceManagement commute = mainPageAttendanceRepository.attendanceCommute(employeeCode);
//
//        if (commute == null) {
//
//            commute = new AttendanceManagement();
//            commute.setAttendanceManagementArrivalTime(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
//
//        }
//
//        AttendanceManagementDTO commutes = modelMapper.map(commute, AttendanceManagementDTO.class);

        return null;

    }


    @Transactional
    public InsertAttendanceManagementDTO saveArrive(InsertAttendanceManagementDTO managementDTO, User user) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeCode(user.getEmployeeCode());
        managementDTO.setAttendanceEmployeeCode(employeeDTO);

        System.out.println("====== managementDTO ==== " + managementDTO);
        InsertAttendanceManagement attendanceManagementEntity = modelMapper.map(managementDTO, InsertAttendanceManagement.class);

        System.out.println("8888888  ====> attendanceManagementEntity = " + attendanceManagementEntity);

        InsertAttendanceManagement insertAttendanceManagement = mainPageAttendanceRepository.save(attendanceManagementEntity);
        System.out.println("insertAttendanceManagement = " + insertAttendanceManagement);

        return modelMapper.map(insertAttendanceManagement,InsertAttendanceManagementDTO.class);
    }



    @Transactional
    public InsertAttendanceManagementDTO saveOnlyDeparture(InsertAttendanceManagementDTO managementDTO, User user) {


        int employeeNum = user.getEmployeeCode();
        InsertAttendanceManagement updateAttendance = mainPageAttendanceRepository.findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(employeeNum);

        // 출퇴근 정보 업데이트
        updateAttendance.setAttendanceManagementDepartureTime(managementDTO.getAttendanceManagementDepartureTime());

        // 변경된 엔티티를 저장
        mainPageAttendanceRepository.save(updateAttendance);

        // 업데이트된 정보를 DTO에 반영하여 반환
        managementDTO.setAttendanceManagementDepartureTime(updateAttendance.getAttendanceManagementDepartureTime());

        return managementDTO;

    }

    @Transactional
    public InsertAttendanceManagementDTO saveDeparture(InsertAttendanceManagementDTO managementDTO, User user) {

        int employeeNum = user.getEmployeeCode();
        InsertAttendanceManagement updateAttendance = mainPageAttendanceRepository.findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(employeeNum);

        // 출퇴근 정보 업데이트
        updateAttendance.setAttendanceManagementDepartureTime(managementDTO.getAttendanceManagementDepartureTime());
        updateAttendance.setAttendanceManagementState(managementDTO.getAttendanceManagementState());

        // 변경된 엔티티를 저장
        mainPageAttendanceRepository.save(updateAttendance);

        // 업데이트된 정보를 DTO에 반영하여 반환
        managementDTO.setAttendanceManagementDepartureTime(updateAttendance.getAttendanceManagementDepartureTime());
        managementDTO.setAttendanceManagementState(updateAttendance.getAttendanceManagementState());

        return managementDTO;


    }


}
