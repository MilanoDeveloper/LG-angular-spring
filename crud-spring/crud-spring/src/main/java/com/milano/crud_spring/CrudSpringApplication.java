package com.milano.crud_spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.milano.crud_spring.enums.Category;
import com.milano.crud_spring.model.Course;
import com.milano.crud_spring.model.Lesson;
import com.milano.crud_spring.repository.CourseRepository;

@SpringBootApplication
public class CrudSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudSpringApplication.class, args);
	}

	@Bean
	CommandLineRunner initDataBase(CourseRepository courseRepository){
		return args -> {

			courseRepository.deleteAll();

			Course c = new Course();
			c.setName("Angular com Spring");
			c.setCategory(Category.FRONT_END);

			Lesson l = new Lesson();
			l.setName("Intro");
			l.setYoutubeUrl("https://www.youtube.com/watch?v=ioLhoRJfyq8&ab_channel=MilanoExplora");
			l.setCourse(c);
			c.getLessons().add(l);

			Lesson l2 = new Lesson();
			l2.setName("Angular");
			l2.setYoutubeUrl("https://www.youtube.com/watch?v=ioLhoRJfyq8&ab_channel=MilanoExplora");
			l2.setCourse(c);
			c.getLessons().add(l2);

			courseRepository.save(c);
		};
	}
}
