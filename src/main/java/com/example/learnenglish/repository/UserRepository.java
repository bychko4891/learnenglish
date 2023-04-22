package com.example.learnenglish.repository;

import com.example.learnenglish.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findById(long id);
    @Query("select u.password from User u where u.email = :username")
    String getPasswordByUsername(String username);
}
