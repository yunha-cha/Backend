package com.wittypuppy.backend.admin.service;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.admin.dto.*;

import com.wittypuppy.backend.admin.entity.*;
import com.wittypuppy.backend.admin.repository.*;


import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdminService {
    private final AdminEmployeeRepository employeeRepository;
    private final AdminCareerRepository careerRepository;
    private final AdminEducationRepository educationRepository;
    private final AdminBoardRepository boardRepository;
    private final AdminDepartmentRepository departmentRepository;
    private final AdminEmailRepository emailRepository;
    private final AdminJobRepository jobRepository;
    private final AdminProfileRepository profileRepository;
    private final ModelMapper modelMapper;

    public AdminService(AdminEmployeeRepository repository, AdminCareerRepository careerRepository, AdminEducationRepository educationRepository, AdminBoardRepository boardRepository, AdminDepartmentRepository departmentRepository, AdminEmailRepository emailRepository, AdminJobRepository jobRepository, AdminProfileRepository profileRepository, ModelMapper modelMapper) {
        this.employeeRepository = repository;
        this.careerRepository = careerRepository;
        this.educationRepository = educationRepository;
        this.boardRepository = boardRepository;
        this.departmentRepository = departmentRepository;
        this.emailRepository = emailRepository;
        this.jobRepository = jobRepository;
        this.profileRepository = profileRepository;
        this.modelMapper = modelMapper;
    }

    public EmployeeDTO getUserInfo(EmployeeDTO employeeDTO) {

        return modelMapper.map(employeeRepository.findById(employeeDTO.getEmployeeCode()),EmployeeDTO.class);
    }
    @Transactional
    public EmployeeDTO updateUserInfo(EmployeeDTO employeeDTO) {
        //엔티티로 변환 후 save
        Employee employee = employeeRepository.save(modelMapper.map(employeeDTO,Employee.class));
        //DTO로 변환 후 return
        return modelMapper.map(employeeRepository.save(employee),EmployeeDTO.class);
    }
    @Transactional
    public EmployeeDTO createUser(EmployeeDTO employeeDTO) {
//        Department departmentEntity = departmentRepository.findByDepartmentName(employeeDTO.getEmployeeDepartment().getDepartmentName());
//        Job jobEntity = jobRepository.findByJobName(employeeDTO.getEmployeeJob().getJobName());

//        Employee employeeEntity = modelMapper.map(employeeDTO,Employee.class);
//        employeeEntity.setDepartment(departmentEntity);
//        employeeEntity.setJob(jobEntity);
        System.out.println(employeeDTO.getEmployeePassword());
        String newHashPwd = BCrypt.hashpw(employeeDTO.getEmployeePassword(), BCrypt.gensalt());
        employeeDTO.setEmployeePassword(newHashPwd);
        System.out.println(employeeDTO.getEmployeePassword());

        Employee employee = employeeRepository.save(modelMapper.map(employeeDTO,Employee.class));

        return modelMapper.map(employee,EmployeeDTO.class);
    }
    @Transactional
    public EducationDTO createUserEducation(EducationDTO educationDTO) {
        Education education = educationRepository.save(modelMapper.map(educationDTO, Education.class));
        return modelMapper.map(education,EducationDTO.class);
    }

    @Transactional
    public CareerDTO createUserCareer(CareerDTO careerDTO) {
        Career career = careerRepository.save(modelMapper.map(careerDTO,Career.class));
        return modelMapper.map(career, CareerDTO.class);
    }
    //import 바꿔라
    public BoardDTO findById(Long boardCode) {
        return modelMapper.map(boardRepository.findById(boardCode),BoardDTO.class);
    }

    public BoardDTO allowBoard(BoardDTO boardDTO) {
        return modelMapper.map(boardRepository.save(modelMapper.map(boardDTO, Board.class)),BoardDTO.class);
    }

    public List<BoardDTO> showNeedAllowBoard() {
        return convert(boardRepository.findAllByBoardAccessStatus("N"),BoardDTO.class);
    }

    private <S, T> List<T> convert(List<S> list, Class<T> targetClass) {
        return list.stream()
                .map(value -> modelMapper.map(value, targetClass))
                .collect(Collectors.toList());
    }

    public List<DepartmentDTO> getDepartment() {
        return convert(departmentRepository.findAll(),DepartmentDTO.class);
    }
    @Transactional
    public List<EmployeeDTO> sendMailAll(EmailDTO email) {
        List<Employee> employees = employeeRepository.findAll();
        List<EmailDTO> emailList = new ArrayList<>();
        for(Employee a : employees){    //유저들 길이만큼 반복
            emailList.add(email);   //이메일 리스트에 유저 길이 만큼 add
        }
        for(int i=0; i<emailList.size(); i++){
            emailList.get(i).setEmailTitle("관리자 전체 발송");
            EmployeeDTO employeeDTO = new EmployeeDTO();


            emailList.get(i).setEmailReceiver(employeeDTO);

            emailList.get(i).setEmailContent(email.getEmailContent());
            System.out.println(emailList.get(i));
        }
        List<Email> emails = convert(emailList,Email.class);
        for(int i=0; i<emails.size(); i++){
            emails.get(i).setEmailReceiver(employees.get(i));
            emails.get(i).setEmailSendTime(LocalDateTime.now());
            System.out.println(emails.get(i).getEmailReceiver().getEmployeeCode());
        }
        emailRepository.saveAll(emails);
        return convert(employees,EmployeeDTO.class);
    }
    public List<EmployeeDTO> sendDepartmentMail(EmailDTO email) {

        System.out.println(email.getStatus());
        List<Employee> employees = employeeRepository.findEmployee(email.getStatus());
        List<EmployeeDTO> employeeDTO = convert(employees, EmployeeDTO.class);
        for(EmployeeDTO emp : employeeDTO){
            System.out.println(emp);
        }

        List<Email> emails = new ArrayList<>();
        for(int i=0; i<employees.size(); i++){
            Email emailEntity = new Email();
            Employee employeeEntity = new Employee();
            employeeEntity.setEmployeeCode(15L);

            emailEntity.setEmailReceiver(employees.get(i));
            emailEntity.setEmailSender(employeeEntity);
            emailEntity.setEmailContent(email.getEmailContent());
            emailEntity.setEmailTitle("관리자 단체 발송");
            emailEntity.setEmailReadStatus("N");
            emailEntity.setEmailSendTime(LocalDateTime.now());
            emailEntity.setEmailStatus("send");
            emails.add(emailEntity);
        }
        emailRepository.saveAll(emails);
        return convert(employees,EmployeeDTO.class);
    }

    public List<EmployeeDTO> sendMailAll2(EmailDTO email) {
        List<Employee> employee = employeeRepository.findAllByDepartment_DepartmentName(email.getStatus2());
        List<EmployeeDTO> employeeDTO = convert(employee, EmployeeDTO.class);
        for(EmployeeDTO emp : employeeDTO){
            System.out.println(emp);
        }

        List<Email> emails = new ArrayList<>();
        for(int i=0; i<employee.size(); i++){
            Email emailEntity = new Email();
            Employee employeeEntity = new Employee();
            employeeEntity.setEmployeeCode(15L);

            emailEntity.setEmailReceiver(employee.get(i));
            emailEntity.setEmailSender(employeeEntity);
            emailEntity.setEmailContent(email.getEmailContent());
            emailEntity.setEmailTitle("관리자 단체 발송");
            emailEntity.setEmailReadStatus("N");
            emailEntity.setEmailSendTime(LocalDateTime.now());
            emailEntity.setEmailStatus("send");
            emails.add(emailEntity);
        }
        emailRepository.saveAll(emails);
        return employeeDTO;
    }

    public ProfileDTO insertProfile(ProfileDTO profileDTO, User user) {
        Employee employee = employeeRepository.findById((long)user.getEmployeeCode()).orElseThrow(null);
        profileDTO.setEmployee(modelMapper.map(employee,EmployeeDTO.class));
        return modelMapper.map(profileRepository.save(modelMapper.map(profileDTO,Profile.class)),ProfileDTO.class);
    }
}
