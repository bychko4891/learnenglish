package com.example.learnenglish.repository;

import com.example.learnenglish.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
    @Query("select u.password from User u where u.email = :username")
    String getPasswordByUsername(String username);

}
