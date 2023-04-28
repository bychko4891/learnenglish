package com.example.learnenglish.service;

import com.example.learnenglish.model.users.Role;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.model.users.UserStatistics;
import com.example.learnenglish.repository.UserRepository;
import com.example.learnenglish.repository.UserStatisticsRepository;
import com.example.learnenglish.responsestatus.Message;
import com.example.learnenglish.responsestatus.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserStatisticsRepository userStatisticsRepository;

    public boolean createUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new User with email: {}", email);
        user.setStatistics(new UserStatistics());
        userRepository.save(user);
        return true;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(long id) {
        return userRepository.findById(id);
    }

    public void updateUser(Long userId, String firstName, String lastName) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLastName(lastName);
            user.setFirstName(firstName);
            userRepository.save(user);
//            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
//        userRepository.save(user);
    }

    public ResponseStatus updateUserPassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String encodedPassword = userRepository.getPasswordByUsername(user.getEmail());
            if (passwordEncoder.matches(oldPassword, encodedPassword)) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return new ResponseStatus(Message.SUCCESS_UPDATEPASSWORD);
            } else return new ResponseStatus(Message.ERROR_UPDATEPASSWORD);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
//        userRepository.save(user);
    }
public void saveUserAvatar(Long userId, String avatarePath){
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        user.setAvatarePath(avatarePath);
        userRepository.save(user);
//            return userRepository.save(user);
    } else {
        throw new IllegalArgumentException("User with id " + userId + " not found");
    }
}

}
