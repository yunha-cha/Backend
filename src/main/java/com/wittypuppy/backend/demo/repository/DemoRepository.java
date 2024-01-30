package com.wittypuppy.backend.demo.repository;

import com.wittypuppy.backend.demo.dto.DemoDTO;
import com.wittypuppy.backend.demo.entity.Demo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemoRepository extends JpaRepository<Demo,Long> {

}
