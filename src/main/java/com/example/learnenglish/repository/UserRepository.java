package com.example.learnenglish.repository;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(long id);

    @Query("SELECT u FROM User u WHERE u.id <> 1 ORDER BY u.id ASC")
    Page<User> findAll(Pageable pageable);

    User findByActivationCode(String code);
}
