package com.raihanhori.library_app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raihanhori.library_app.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Optional<Category> findFirstByName(String name);
	
}
