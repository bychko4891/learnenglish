package com.example.learnenglish.service;

import com.example.learnenglish.model.users.Role;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.UserRepository;
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

    public boolean createUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new User with email: {}", email);
        userRepository.save(user);
        return true;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findById(long id){
     return userRepository.findById(id);
    }

    public void updateUser(Long userId, String firstName, String lastName){
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



}
