package com.wittypuppy.backend.demo.service;

import com.wittypuppy.backend.common.exception.DataDeletionException;
import com.wittypuppy.backend.common.exception.DataInsertionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import com.wittypuppy.backend.demo.dto.DemoDTO;
import com.wittypuppy.backend.demo.entity.Demo;
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
            throw new DataInsertionException("Demo 데이터 입력 중 에러 발생");
        }
    }

    @Transactional
    public String updateDemo(DemoDTO demoDTO, Long demoCode) {
        log.info("DemoService >>> updateDemo >>> start");

        try {
            Demo demo = demoRepository.findById(demoCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 Demo 데이터를 찾을 수 없습니다."));
            demo.column1(demoDTO.getColumn1())
                    .column2(demoDTO.getColumn2())
                    .column3(demoDTO.getColumn3())
                    .column4(demoDTO.getColumn4());
            log.info("DemoService >>> updateDemo >>> end");
            return "상품 수정 성공";
        } catch (Exception e) {
            log.error("DemoService >>> updateDemo >>> Error >>>", e);
            throw new DataUpdateException("Demo 데이터 갱신 중 에러 발생");
        }
    }

    @Transactional
    public String deleteDemoByNo(Long demoCode) {
        try {
            Demo demo = demoRepository.findById(demoCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 Demo 데이터를 찾을 수 없습니다."));
            demoRepository.delete(demo);
            log.info("DemoService >>> updateDemo >>> end");
            return "상품 삭제 성공";
        } catch (Exception e) {
            log.error("DemoService >>> updateDemo >>> Error >>>", e);
            throw new DataDeletionException("Demo 데이터 삭제 중 에러 발생");
        }
    }
}
