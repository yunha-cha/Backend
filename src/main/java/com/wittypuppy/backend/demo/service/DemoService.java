package com.wittypuppy.backend.demo.service;

import com.wittypuppy.backend.demo.dto.DemoDTO;
import com.wittypuppy.backend.demo.entity.Demo;
import com.wittypuppy.backend.demo.exception.DemoException;
import com.wittypuppy.backend.demo.repository.DemoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DemoService {
    private final DemoRepository demoRepository;
    private final ModelMapper modelMapper;

    public DemoService(DemoRepository demoRepository, ModelMapper modelMapper) {
        this.demoRepository = demoRepository;
        this.modelMapper = modelMapper;
    }

    public List<DemoDTO> selectDemoList() {
        log.info("DemoService >>> selectDemoList >>> start");

        List<Demo> demoList = demoRepository.findAll();

        List<DemoDTO> demoDTOList = demoList.stream()
                .map(demo -> modelMapper.map(demo, DemoDTO.class))
                .collect(Collectors.toList());

        log.info("DemoService >>> selectDemoList >>> end");
        return demoDTOList;
    }

    public DemoDTO findDemoByNo(Long demoCode) {
        log.info("DemoService >>> findDemoByNo >>> start");

        Demo demo = demoRepository.findById(demoCode).get();
        System.out.println(demo);
        DemoDTO demoDTO = modelMapper.map(demo, DemoDTO.class);
        System.out.println(demoDTO);
        log.info("DemoService >>> findDemoByNo >>> end");
        return demoDTO;
    }

    @Transactional
    public String insertDemo(DemoDTO demoDTO) {
        log.info("DemoService >>> insertDemo >>> start");

        Demo insertDemo = modelMapper.map(demoDTO, Demo.class);

        try {
            demoRepository.save(insertDemo);
            log.info("DemoService >>> insertDemo >>> end");

            return "상품 입력 성공";
        } catch (Exception e) {
            log.error("DemoService >>> insertDemo >>> Error >>>", e);
            return "상품 입력 실패";
        }
    }

    @Transactional
    public String updateDemo(DemoDTO demoDTO, Long demoCode) {
        log.info("DemoService >>> updateDemo >>> start");

        try {
            Demo demo = demoRepository.findById(demoCode).get();
            demo.column1(demoDTO.getColumn1())
                    .column2(demoDTO.getColumn2())
                    .column3(demoDTO.getColumn3())
                    .column4(demoDTO.getColumn4());
            log.info("DemoService >>> updateDemo >>> end");
            return "상품 수정 성공";
        } catch (Exception e) {
            log.error("DemoService >>> updateDemo >>> Error >>>", e);
            return "상품 수정 실패";
        }
    }

    @Transactional
    public String deleteDemoByNo(Long demoCode) {
        try {

            Optional<Demo> optionalDemo = demoRepository.findById(demoCode);
            if (optionalDemo.isPresent()) {
                Demo demo = optionalDemo.get();
                demoRepository.delete(demo);
                log.info("DemoService >>> updateDemo >>> end");
                return demoCode + "번 상품 삭제 성공";
            } else {

                log.info("DemoService >>> updateDemo >>> Does not Exist >>> ");
//                return demoCode + "번 상품이 없습니다.";

                throw new DemoException();
            }
        } catch (Exception e) {
            log.error("DemoService >>> updateDemo >>> Error >>>", e);
            return demoCode + "번 상품 삭제 실패";
        }
    }
}
