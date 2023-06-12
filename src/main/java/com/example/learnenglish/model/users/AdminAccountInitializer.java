package com.example.learnenglish.model.users;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class AdminAccountInitializer implements ApplicationRunner {
    @Value(("${user.admin.email}"))
    private String adminEmail;

    @Value(("${user.admin.password}"))
    private String adminPassword;


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAccountInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setEmail(adminEmail);
            admin.setActive(true);
            admin.setUserTextInLesson(false);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.getAuthority().add(Role.ROLE_ADMIN);
            admin.getGender().add(UserGender.MALE);
            UserStatistics userStatistics = new UserStatistics();
            userStatistics.setStudyTimeInTwoWeeks(new ArrayList<>(Arrays.asList(0)));
            userStatistics.setTrainingDaysInMonth(new ArrayList<>(Arrays.asList(LocalDate.now())));
            admin.setStatistics(userStatistics);
            admin.setUserAvatar(new UserAvatar());
            userRepository.save(admin);
        }
    }
}
