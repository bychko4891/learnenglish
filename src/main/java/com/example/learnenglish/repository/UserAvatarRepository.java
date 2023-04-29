package com.example.learnenglish.repository;

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.model.users.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAvatarRepository extends JpaRepository<UserAvatar, Long> {
}
