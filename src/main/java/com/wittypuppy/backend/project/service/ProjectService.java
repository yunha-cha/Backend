package com.wittypuppy.backend.project.service;

import com.wittypuppy.backend.common.exception.DataInsertionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.project.dto.ProjectAndMemberAndPostAndPostMemberDTO;
import com.wittypuppy.backend.project.dto.ProjectAndProjectMemberDTO;
import com.wittypuppy.backend.project.entity.ProjectAndMemberAndPostAndPostMember;
import com.wittypuppy.backend.project.entity.ProjectAndProjectMember;
import com.wittypuppy.backend.project.exception.ProjectIsLockedException;
import com.wittypuppy.backend.project.repository.EmployeeRepository;
import com.wittypuppy.backend.project.repository.ProjectAndMemberAndPostAndPostMemberRepository;
import com.wittypuppy.backend.project.repository.ProjectAndProjectMemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ProjectService {
    private final ProjectAndProjectMemberRepository projectAndProjectMemberRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectAndMemberAndPostAndPostMemberRepository projectAndMemberAndPostAndPostMemberRepository;
    private final ModelMapper modelMapper;

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

        List<ProjectAndProjectMemberDTO> resultDTO = result.stream().map(e -> modelMapper.map(e, ProjectAndProjectMemberDTO.class)).toList();
        log.info("ProjectAndProjectMemberDTO >>> {} ", result);

        log.info("[ProjectService] >>> selectProductListByConditionAndSearchValue >>> end");
        return resultDTO;
    }

    @Transactional
    public String createNewProject(ProjectAndProjectMemberDTO projectAndProjectMemberDTO) {
        log.info("[ProjectService] >>> createNewProject >>> start");
        int result = 0;

        try {
            ProjectAndProjectMember projectAndProjectMember = modelMapper.
                    map(projectAndProjectMemberDTO, ProjectAndProjectMember.class);
            projectAndProjectMemberRepository.save(projectAndProjectMember);
            result = 1;
        } catch (Exception e) {
            throw new DataInsertionException("프로젝트 추가에 실패했습니다.");
        }

        log.info("[ProjectService] >>> createNewProject >>> end");
        return result > 0 ? "프로젝트 추가에 성공했습니다." : "프로젝트 추가에 실패했습니다.";
    }

    public ProjectAndMemberAndPostAndPostMemberDTO selectProject(Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> createNewProject >>> start");

        ProjectAndMemberAndPostAndPostMember result
                = projectAndMemberAndPostAndPostMemberRepository.findById(projectCode).
                orElseThrow(() -> new DataNotFoundException("프로젝트가 존재하지 않습니다."));

        if (!result.getProjectManagerCode().equals(employeeCode) &&
                !result.getProjectMemberAndPostMemberList().contains(employeeCode) &&
                result.getProjectLockedStatus().equals("Y")) {
            // 프로젝트 관리자도 아니고
            // 프로젝트 멤버도 아니고
            // 프로젝트가 잠궈져 있다면.
            // --> 프로젝트 관리자이면 접근 가능
            // --> 프로젝트 멤버이면 접근 가능
            // --> 프로젝트가 잠궈져 있지 않다면 접근 가능

            throw new ProjectIsLockedException("프로젝트 관계자만 접근 가능합니다.");
        }

        ProjectAndMemberAndPostAndPostMemberDTO resultDTO
                = modelMapper.map(result, ProjectAndMemberAndPostAndPostMemberDTO.class);

        log.info("ProjectAndProjectMemberDTO >>> {} ", resultDTO);

        log.info("[ProjectService] >>> selectProductListByConditionAndSearchValue >>> end");
        return resultDTO;
    }
}
