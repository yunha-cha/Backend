package com.wittypuppy.backend.mainpage.repository;

import com.wittypuppy.backend.mainpage.entity.MainPagePost;
import com.wittypuppy.backend.mainpage.entity.MainPageProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainPageProjectRepository extends JpaRepository<MainPageProject,Long> {
    List<MainPageProject> findAllByOrderByProjectPostList_projectPostCreationDateDesc();
}
