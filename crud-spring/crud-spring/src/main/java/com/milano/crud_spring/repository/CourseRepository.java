package com.milano.crud_spring.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.milano.crud_spring.enums.Status;
import com.milano.crud_spring.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{

    Page<Course> findByStatus(Pageable pageable, Status status);

    List<Course> findByName(String name);
}
