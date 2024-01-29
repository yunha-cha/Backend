package com.wittypuppy.backend.project.service;

import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.project.dto.ProjectDTO;
import com.wittypuppy.backend.project.entity.Employee;
import com.wittypuppy.backend.project.entity.Project;
import com.wittypuppy.backend.project.entity.ProjectMember;
import com.wittypuppy.backend.project.exception.CreateProjectException;
import com.wittypuppy.backend.project.exception.ModifyProjectException;
import com.wittypuppy.backend.project.exception.ProjectLockedException;
import com.wittypuppy.backend.project.repository.EmployeeRepository;
import com.wittypuppy.backend.project.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public List<ProjectDTO> selectProjectListByTypeAndSearchValue(String type, String searchValue, Long employeeCode) {
        log.info("[ProjectService] >>> selectProjectListByTypeAndSearchValue >>> start");
        List<Project> projectList = null;
        if (searchValue == null || searchValue.isBlank()) {
            if (type == null) {
                projectList = projectRepository.findAll();
            } else if (type.equals("me")) {
                projectList = projectRepository.findAllByProjectMemberList_EmployeeCode(employeeCode);
            } else if (type.equals("myteam")) {
                Long myDepartmentCode = employeeRepository.findById(employeeCode).orElseThrow(() -> new DataNotFoundException("사원 정보가 잘못 되었습니다."))
                        .getDepartment().getDepartmentCode();
                List<Long> employeeCodeList = employeeRepository.findAllByDepartment_DepartmentCode(myDepartmentCode).stream().map(Employee::getEmployeeCode).collect(Collectors.toList());
                projectList = projectRepository.findAllByProjectMemberList_EmployeeCodeIn(employeeCodeList);
            }
        } else {
            if (type == null) {
                projectList = projectRepository.findAllByProjectTitle(searchValue);
            } else if (type.equals("me")) {
                projectList = projectRepository.findAllByProjectTitleAndProjectMemberList_EmployeeCode(searchValue, employeeCode);
            } else if (type.equals("myteam")) {
                Long myDepartmentCode = employeeRepository.findById(employeeCode).orElseThrow(() -> new DataNotFoundException("사원 정보가 잘못 되었습니다."))
                        .getDepartment().getDepartmentCode();
                List<Long> employeeCodeList = employeeRepository.findAllByDepartment_DepartmentCode(myDepartmentCode).stream().map(Employee::getEmployeeCode).collect(Collectors.toList());
                projectList = projectRepository.findAllByProjectTitleAndProjectMemberList_EmployeeCodeIn(searchValue, employeeCodeList);
            }
        }

        List<ProjectDTO> projectDTOList = projectList.stream().map(project -> modelMapper.map(project, ProjectDTO.class)).collect(Collectors.toList());

        log.info("[ProjectService] >>> selectProjectListByTypeAndSearchValue >>> end");

        return projectDTOList;
    }

    @Transactional
    public String createProject(ProjectDTO projectDTO, Long employeeCode) {
        log.info("[ProjectService] >>> selectProjectListByTypeAndSearchValue >>> start");

        int result;
        try {
            Employee employee = employeeRepository.findById(employeeCode).orElseThrow(() -> new DataNotFoundException("해당하는 사원 정보가 없습니다."));
            Project newProject = modelMapper.map(projectDTO, Project.class);
            newProject.setProjectManager(employee);
            ProjectMember projectMember = new ProjectMember();
            projectMember.setEmployee(employee);
            projectMember.setProjectMemberDeleteStatus("N");
            newProject.setProjectMemberList(Stream.of(projectMember).collect(Collectors.toList()));

            projectRepository.save(newProject);
            result = 1;
        } catch (Exception e) {
            throw new CreateProjectException("프로젝트 생성에 실패했습니다.");
        }

        log.info("[ProjectService] >>> selectProjectListByTypeAndSearchValue >>> end");
        return result > 0 ? "프로젝트 생성 성공" : "프로젝트 생성 실패";
    }

    public ProjectDTO selectProjectByProjectCode(Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> selectProjectByProjectCode >>> start");

        Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당하는 프로젝트를 찾을 수 없습니다."));

        List<ProjectMember> projectMemberList = project.getProjectMemberList();

        boolean isLocked = project.getProjectLockedStatus().equals("Y");
        List<Long> employeeCodeList = projectMemberList.stream().map(projectMember -> projectMember.getEmployee().getEmployeeCode()).toList();
        boolean isMember = employeeCodeList.contains(employeeCode);
        if (isLocked && !isMember) {
            throw new ProjectLockedException("허가되지 않은 사용자입니다.");
        }
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        log.info("[ProjectService] >>> selectProjectByProjectCode >>> end");
        return projectDTO;
    }

    @Transactional
    public String modifyProject(ProjectDTO projectDTO, Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> selectProjectByProjectCode >>> start");
        int result;

        try{
            Project project = projectRepository.findById(projectCode).orElseThrow(()->new DataNotFoundException("해당 프로젝트를 찾을 수 없습니다."));
            project.setProjectTitle(projectDTO.getProjectTitle());
            project.setProjectDescription(projectDTO.getProjectDescription());
            project.setProjectProgressStatus(projectDTO.getProjectProgressStatus());
            project.setProjectDeadline(projectDTO.getProjectDeadline());
            project.setProjectLockedStatus(projectDTO.getProjectLockedStatus());
            result = 1;
        }catch(Exception e){
            throw new ModifyProjectException("프로젝트 수정에 실패했습니다.");
        }


        log.info("[ProjectService] >>> selectProjectByProjectCode >>> end");

        return result > 0 ? "프로젝트 수정 성공" : "프로젝트 수정 실패";
    }
}
