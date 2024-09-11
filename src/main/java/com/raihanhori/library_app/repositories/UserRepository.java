package com.raihanhori.library_app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raihanhori.library_app.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findFirstByEmail(String email);
}
