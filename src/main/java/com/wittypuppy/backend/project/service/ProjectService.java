package com.wittypuppy.backend.project.service;

import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.project.dto.ProjectAndProjectMemberDTO;
import com.wittypuppy.backend.project.entity.ProjectAndProjectMember;
import com.wittypuppy.backend.project.repository.EmployeeRepository;
import com.wittypuppy.backend.project.repository.ProjectAndProjectMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class
ProjectService {
    private final ProjectAndProjectMemberRepository projectAndProjectMemberRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public ProjectService(ProjectAndProjectMemberRepository projectAndProjectMemberRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.projectAndProjectMemberRepository = projectAndProjectMemberRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public List<ProjectAndProjectMemberDTO> selectProductListByConditionAndSearchValue(String condition, String searchValue, Long employeeCode) {
        log.info("[ProjectService] >>> selectProductListByConditionAndSearchValue >>> start");
        List<ProjectAndProjectMember> result = null;
        if (Objects.isNull(searchValue) || searchValue.isBlank()) {
            log.info("검색어가 없는 경우");
            if (Objects.isNull(condition) || condition.isBlank()) {
                log.info("전체 프로젝트 목록 조회인 경우.");
                result = projectAndProjectMemberRepository.findProjectAndProjectMember();
            } else if (condition.equals("me")) {
                log.info("내 프로젝트만 조회하는 경우");
                result = projectAndProjectMemberRepository.findProjectAndProjectMemberByEmployeeCode(employeeCode);
            } else if (condition.equals("myteam")) {
                log.info("내 부서의 프로젝트만 조회하는 경우");
                try {
                    Long departmentCode = employeeRepository.findById(employeeCode).get().getDepartmentCode();
                    List<Long> employeeCodeListByDepartmentCode = employeeRepository.findAllByDepartmentCode(departmentCode)
                            .stream().map(e -> e.getEmployeeCode()).collect(Collectors.toList());
                    result = projectAndProjectMemberRepository.findProjectAndProjectMemberByDepartmentCode(employeeCodeListByDepartmentCode);
                } catch (Exception e) {
                    throw new DataNotFoundException("내 부서 정보가 없거나 부서에 사원 정보가 없습니다.");
                }
            }
        } else {
            log.info("검색어가 있는 경우");
            if (Objects.isNull(condition) || condition.isBlank()) {
                log.info("전체 프로젝트 목록 조회인 경우.");
                result = projectAndProjectMemberRepository.findProjectAndProjectMemberBySearchValue(searchValue);
            } else if (condition.equals("me")) {
                log.info("내 프로젝트만 조회하는 경우");
                result = projectAndProjectMemberRepository.findProjectAndProjectMemberByEmployeeCodeAndSearchValue(employeeCode, searchValue);
            } else if (condition.equals("myteam")) {
                log.info("내 부서의 프로젝트만 조회하는 경우");
                try {
                    Long departmentCode = employeeRepository.findById(employeeCode).get().getDepartmentCode();
                    List<Long> employeeCodeListByDepartmentCode = employeeRepository.findAllByDepartmentCode(departmentCode)
                            .stream().map(e -> e.getEmployeeCode()).collect(Collectors.toList());
                    result = projectAndProjectMemberRepository.findProjectAndProjectMemberByDepartmentCodeAndSearchValue(employeeCodeListByDepartmentCode, searchValue);
                } catch (Exception e) {
                    throw new DataNotFoundException("내 부서 정보가 없거나 부서에 사원 정보가 없습니다.");
                }
            }
        }

        log.info("ProjectAndProjectMember >>> {} ", result);

        List<ProjectAndProjectMemberDTO> resultDTO = result.stream().map(e -> modelMapper.map(e, ProjectAndProjectMemberDTO.class)).toList();
        log.info("ProjectAndProjectMemberDTO >>> {} ", result);

        log.info("[ProjectService] >>> selectProductListByConditionAndSearchValue >>> end");
        return resultDTO;
    }
}
