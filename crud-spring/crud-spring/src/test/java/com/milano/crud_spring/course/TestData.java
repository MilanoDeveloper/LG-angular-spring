package com.milano.crud_spring.course;

import java.util.List;

import com.milano.crud_spring.enums.Category;
import com.milano.crud_spring.enums.Status;
import com.milano.crud_spring.model.Course;
import com.milano.crud_spring.model.Lesson;

public class TestData {

    private static final String COURSE_NAME = "Spring";
    private static final String INVALID_COURSE_NAME = "Spr";
    private static final String LOREN_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc et quam nec diam tristique mollis eget quis urna. Sed dapibus lectus in arcu rutrum, non luctus sem finibus. Cras nisl neque, pellentesque et tortor id, dapibus auctor turpis.";

    private static final String LESSON_NAME = "Spring Intro";
    private static final String LESSON_YOUTUBE = "abcdefgh123";

    private TestData() {
    }

    public static Course createValidCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setName(COURSE_NAME);
        course.setCategory(Category.BACK_END);
        course.setStatus(Status.ACTIVE);

        Lesson lesson = new Lesson();
        lesson.setName(LESSON_NAME);
        lesson.setYoutubeUrl(LESSON_YOUTUBE);
        lesson.setCourse(course);
        return course;
    }

    public static List<Course> createInvalidCourses() {
        final String validName = COURSE_NAME;
        final String empty = "";

        return List.of(
                buildCourse(null, null),
                buildCourse(null, Category.BACK_END),
                buildCourse(empty, Category.BACK_END),
                buildCourse(INVALID_COURSE_NAME, Category.BACK_END),
                buildCourse(LOREN_IPSUM, Category.BACK_END),
                buildCourse(validName, null),
                buildCourse(validName, Category.BACK_END));
    }

    private static Course buildCourse(String name, Category category) {
        Course course = new Course();
        course.setName(name);
        course.setCategory(category);
        return course;
    }

}