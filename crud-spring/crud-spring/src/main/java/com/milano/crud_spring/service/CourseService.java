package com.milano.crud_spring.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.milano.crud_spring.dto.CourseDTO;
import com.milano.crud_spring.dto.CoursePageDTO;
import com.milano.crud_spring.dto.mapper.CourseMapper;
import com.milano.crud_spring.exception.RecordNotFoundException;
import com.milano.crud_spring.model.Course;
import com.milano.crud_spring.model.Lesson;
import com.milano.crud_spring.repository.CourseRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Validated
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public CoursePageDTO list(@PositiveOrZero int page, @Positive @Max(100) int pageSize) {
        Page<Course> pageCourse = courseRepository.findAll(PageRequest.of(page, pageSize));
        List<CourseDTO> courses = pageCourse.get().map(courseMapper::toDTO).collect(Collectors.toList());
        System.out.println("Entrou no Metodo SERVICE CoursePageDTO");
        return new CoursePageDTO(courses, pageCourse.getTotalElements(), pageCourse.getTotalPages());
    }

    /*
     * public List<CourseDTO> list() {
     * return courseRepository.findAll()
     * .stream()
     * .map(courseMapper::toDTO)
     * .collect(Collectors.toList());
     * }
     */

     public CoursePageDTO findAll(@PositiveOrZero int page, @Positive @Max(1000) int pageSize) {
        Page<Course> coursePage = courseRepository.findAll(PageRequest.of(page, pageSize));
        List<CourseDTO> list = coursePage.getContent().stream()
                .map(courseMapper::toDTO)
                .toList();
        return new CoursePageDTO(list, coursePage.getTotalElements(), coursePage.getTotalPages());
    }

    public CourseDTO findById(@NotNull @Positive Long id) {
        return courseRepository.findById(id).map(courseMapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public List<CourseDTO> findByName(@NotNull @NotBlank String name) {
        return courseRepository.findByName(name).stream().map(courseMapper::toDTO).toList();
    }

    public CourseDTO create(@Valid @NotNull CourseDTO course) {
        return courseMapper.toDTO(courseRepository.save(courseMapper.toEntity(course)));
    }

    // public CourseDTO update(@NotNull @Positive Long id, @Valid @NotNull CourseDTO
    // courseDTO) {
    // return courseRepository.findById(id)
    // .map(recordFound -> {
    // Course course = courseMapper.toEntity(courseDTO);
    // recordFound.setName(courseDTO.name());
    // recordFound.setCategory(courseMapper.convertCategoryValue(courseDTO.category()));
    // recordFound.getLessons().clear();
    // course.getLessons().forEach(recordFound.getLessons()::add);
    // return courseMapper.toDTO(courseRepository.save(recordFound));
    // }).orElseThrow(() -> new RecordNotFoundException(id));
    // }

    public CourseDTO update(@NotNull @Positive Long id, @Valid @NotNull CourseDTO courseDTO) {
        return courseRepository.findById(id)
                .map(recordFound -> {
                    recordFound.setName(courseDTO.name());
                    recordFound.setCategory(courseMapper.convertCategoryValue(courseDTO.category()));
                    recordFound.getLessons().clear();
                    courseDTO.lessons().forEach(lessonDTO -> {
                        Lesson lesson = courseMapper.toEntity(lessonDTO);
                        lesson.setCourse(recordFound);
                        recordFound.getLessons().add(lesson);
                    });
                    return courseMapper.toDTO(courseRepository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Long id) {
        courseRepository.delete(courseRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }
}
