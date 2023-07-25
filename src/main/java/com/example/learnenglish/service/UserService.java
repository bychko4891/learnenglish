package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.*;
import com.example.learnenglish.repository.UserRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Value(("${application.host}"))
    private String host;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailSenderInApp mailSender;

    private final HttpServletRequest request;

    public boolean createUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email).isPresent()) return false;
        user.setActivationCode(UUID.randomUUID().toString());
        user.setActive(false);
        user.getGender().add(UserGender.MALE);
        user.setUserTextInLesson(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getAuthority().add(Role.ROLE_USER);
        log.info("Saving new User with email: {}", email);
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setStudyTimeInTwoWeeks(new ArrayList<>(Arrays.asList(0)));
        userStatistics.setTrainingDaysInMonth(new ArrayList<>(Arrays.asList(LocalDate.now())));
        user.setStatistics(userStatistics);
        user.setUserAvatar(new Image());
        userRepository.save(user);
        if (!StringUtils.isEmpty(user.getEmail())) {
            String mailText = String.format("Hello, %s \n" + "Welcome to Learn English. Please, visit next link: %s/activate/%s",
                    user.getFirstName(), host, user.getActivationCode());
            mailSender.sendSimpleMessage(user.getEmail(), "Activation code", mailText);
        }
        return true;
    }


    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {

        }
        return userRepository.findByEmail(email).get();
    }

    public User findById(long id) {
        return userRepository.findById(id).get();
    }

    // доробити !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void updateUserInfo(Long userId, String firstName, String lastName, String gender) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Set<UserGender> newGenders = new HashSet<>();
            newGenders.add(UserGender.valueOf(gender.toUpperCase()));
            User user = optionalUser.get();
            user.setLastName(lastName);
            user.setFirstName(firstName);
            user.getGender().clear();
            user.getGender().add(UserGender.valueOf(gender.toUpperCase()));
            userRepository.save(user);
//            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
//        userRepository.save(user);
    }

    public ResponseMessage updateUserPassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String encodedPassword = user.getPassword();
            if (passwordEncoder.matches(oldPassword, encodedPassword)) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return new ResponseMessage(Message.SUCCESS_UPDATEPASSWORD);
            } else return new ResponseMessage(Message.ERROR_UPDATEPASSWORD);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
//        userRepository.save(user);
    }


    public ResponseMessage userProfileDelete(User user, String userPassword) {
            String encodedPassword = user.getPassword();
            if (passwordEncoder.matches(userPassword, encodedPassword)) {
                userRepository.delete(user);
                logoutUser(request); // Виклик методу розлогінення
                return new ResponseMessage(Message.SUCCESS_UPDATEPASSWORD);
            } else return new ResponseMessage(Message.ERROR_UPDATEPASSWORD);


    }

    private void logoutUser(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, null, auth);
        }
    }

    public Page<User> getUsersPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    public void userActiveEditAdminPage(Long userId, boolean userActive) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {

            User user = optionalUser.get();
            user.setActive(userActive);
            userRepository.save(user);
//            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
    }

    public void saveUserIp(long userId, String ipAddress) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserIp(ipAddress);
            userRepository.save(user);

//            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }

    }

    public ResponseMessage generatePassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            String rawPassword = generateRandomPassword();
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(rawPassword));
            userRepository.save(user);
            String mailText = String.format("Hello, %s \n" + "New password: %s \n" + "to enter the application cabinet %s/login ",
                    user.getFirstName(), rawPassword, host);
            mailSender.sendSimpleMessage(user.getEmail(), "New password fo login", mailText);
            return new ResponseMessage(Message.SUCCESS_FORGOT_PASSWORD);
        } else
            return new ResponseMessage(Message.ERROR_FORGOT_PASSWORD);
    }


    private String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[10];
        secureRandom.nextBytes(randomBytes);
        return Base64.encodeBase64String(randomBytes);
    }


    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActive(true);
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    public ResponseMessage setUserTextInLesson(Long userId, boolean isChecked) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setUserTextInLesson(isChecked);
            userRepository.save(user);
            return new ResponseMessage(Message.SUCCESS_CHECKBOX);
        }else throw new IllegalArgumentException("User with id " + userId + " not found");
    }
}
