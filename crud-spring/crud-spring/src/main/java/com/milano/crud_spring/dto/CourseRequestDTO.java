package com.milano.crud_spring.dto;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.milano.crud_spring.enums.Category;
import com.milano.crud_spring.enums.validation.ValueOfEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CourseRequestDTO(
        @NotBlank @NotNull @Length(min = 5, max = 200) String name,
        @NotBlank @NotNull @ValueOfEnum(enumClass = Category.class) String category,
        @NotNull @NotEmpty @Valid List<LessonDTO> lessons) {
}