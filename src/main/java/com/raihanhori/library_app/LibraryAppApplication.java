package com.raihanhori.library_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.raihanhori.library_app.entity.Category;
import com.raihanhori.library_app.repositories.CategoryRepository;

@SpringBootApplication
@EnableJpaAuditing
public class LibraryAppApplication {
	
	@Autowired
	private CategoryRepository categoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(LibraryAppApplication.class, args);
	}
	
	//setup category
	@Bean
	CommandLineRunner setCategory() {
		return args -> {
			if (categoryRepository.count() == 0) {
				categoryRepository.save(Category.builder().name("Science").build());
				categoryRepository.save(Category.builder().name("Math").build());
				categoryRepository.save(Category.builder().name("Computer Science").build());
				categoryRepository.save(Category.builder().name("History").build());
				categoryRepository.save(Category.builder().name("Literature").build());
				
				System.out.println("Categories are setup successfully!");
			}
			
			System.out.println("Categories are already setup!");
		};
	}

}

