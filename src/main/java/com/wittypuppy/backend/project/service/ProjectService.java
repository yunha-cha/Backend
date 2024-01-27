package com.wittypuppy.backend.project.service;

import com.wittypuppy.backend.common.exception.DataInsertionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import com.wittypuppy.backend.project.dto.ProjectAndProjectMemberDTO;
import com.wittypuppy.backend.project.dto.ProjectDTO;
import com.wittypuppy.backend.project.entity.Project;
import com.wittypuppy.backend.project.entity.ProjectAndProjectMember;
import com.wittypuppy.backend.project.exception.NotProjectManagerException;
import com.wittypuppy.backend.project.exception.ProjectIsLockedException;
import com.wittypuppy.backend.project.repository.EmployeeRepository;
import com.wittypuppy.backend.project.repository.ProjectAndProjectMemberRepository;
import com.wittypuppy.backend.project.repository.ProjectRepository;
import com.wittypuppy.backend.project.repository.ViewProjectInfoRepository;
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
    private final ProjectRepository projectRepository;
    private final ViewProjectInfoRepository viewProjectInfoRepository;
    private final ModelMapper modelMapper;

    public List<ProjectAndProjectMemberDTO> selectProjectListByConditionAndSearchValue(String condition, String searchValue, Long employeeCode) {
        log.info("[ProjectService] >>> selectProjectListByConditionAndSearchValue >>> start");
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

        log.info("[ProjectService] >>> selectProjectListByConditionAndSearchValue >>> end");
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


    public com.wittypuppy.backend.project.dto.viewProjectInfo.ProjectDTO selectProjectByProjectCode(Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> createNewProject >>> start");

        com.wittypuppy.backend.project.entity.viewProjectInfo.Project project =
                viewProjectInfoRepository.findById(projectCode)
                        .orElseThrow(() -> new DataNotFoundException("선택한 프로젝트가 존재하지 않습니다."));
        boolean isLocked = project.getProjectLockedStatus().equals("Y");
        boolean isManager = project.getProjectManager().getEmployeeCode().equals(employeeCode);
        boolean isMember = project.getProjectMemberList().stream().map(projectMember -> projectMember.getProjectMember().getEmployeeCode())
                .toList().get(0).equals(employeeCode);
        /*
        * 잠궈져 있으면서 Manager가 아니고 Member가 아니면 접근 불가능.
        * */
        if(isLocked && !isManager && !isMember){
            throw new ProjectIsLockedException("접근이 불가능한 프로젝트 입니다.");
        }
        com.wittypuppy.backend.project.dto.viewProjectInfo.ProjectDTO projectDTO =
                modelMapper.map(project, com.wittypuppy.backend.project.dto.viewProjectInfo.ProjectDTO.class);

        log.info("[ProjectService] >>> createNewProject >>> end");

        return projectDTO;
    }


    @Transactional
    public String modifyProject(ProjectDTO projectDTO, Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> modifyProject >>> start");
        int result = 0;

        Project project =
                projectRepository.findById(projectCode).
                        orElseThrow(() -> new DataNotFoundException("수정할 프로젝트가 존재하지 않습니다."));
        if (!project.getProjectManagerCode().equals(employeeCode)) {
            throw new NotProjectManagerException("해당 프로젝트를 수정할 권한이 없습니다.");
        }
        try {
            project = project.setProjectTitle(projectDTO.getProjectTitle()).
                    setProjectDescription(projectDTO.getProjectDescription()).
                    setProjectDeadline(projectDTO.getProjectDeadline()).
                    setProjectLockedStatus(projectDTO.getProjectLockedStatus()).
                    setProjectProgressStatus(project.getProjectProgressStatus()).
                    build();
            result = 1;
        } catch (Exception e) {
            throw new DataUpdateException("프로젝트 수정에 실패했습니다.");
        }

        log.info("[ProjectService] >>> modifyProject >>> end");
        return result > 0 ? "프로젝트 수정에 성공했습니다." : "프로젝트 수정에 실패했습니다.";
    }
}
