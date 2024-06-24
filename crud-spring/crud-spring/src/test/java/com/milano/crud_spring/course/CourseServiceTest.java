package com.milano.crud_spring.course;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.milano.crud_spring.config.ValidationAdvice;
import com.milano.crud_spring.dto.mapper.CourseMapper;
import com.milano.crud_spring.exception.RecordNotFoundException;
import com.milano.crud_spring.repository.CourseRepository;
import com.milano.crud_spring.service.CourseService;

import org.springframework.aop.framework.ProxyFactory;

@ActiveProfiles("test")
@SpringJUnitConfig(classes = { CourseService.class, CourseMapper.class })
public class CourseServiceTest {

 @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        ProxyFactory factory = new ProxyFactory(new CourseService(courseRepository, courseMapper));
        factory.addAdvice(new ValidationAdvice());
        courseService = (CourseService) factory.getProxy();
    }

     @Test
    @DisplayName("Should throw NotFound exception when course not found")
    void testFindByIdNotFound() {
        when(this.courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> this.courseService.findById(123L));
        verify(this.courseRepository).findById(anyLong());
    }
}
