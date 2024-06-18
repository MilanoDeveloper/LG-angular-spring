package com.milano.crud_spring.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milano.crud_spring.model.Course;
import com.milano.crud_spring.repository.CourseRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController 
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;


    @GetMapping
    public List<Course> list(){
        return courseRepository.findAll();
    }

}
