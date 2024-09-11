package com.raihanhori.library_app.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.raihanhori.library_app.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	@Query("SELECT b FROM Book b JOIN FETCH b.category c "
		       + "WHERE (:search IS NULL OR b.title LIKE %:search%) "
		       + "AND (:category_id IS NULL OR b.category.id = :category_id)")
	Page<Book> findAllBookWithFilter(@Param("search") String search, @Param("category_id") Integer category_id, Pageable pageable);

	Optional<Book> findFirstByTitle(String title);
	
}
