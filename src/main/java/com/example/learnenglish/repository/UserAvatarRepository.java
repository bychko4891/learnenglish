package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAvatarRepository extends JpaRepository<UserAvatar, Long> {
}
