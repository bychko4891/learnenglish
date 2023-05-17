package com.example.learnenglish.model.users;

import com.example.learnenglish.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountInitializer implements ApplicationRunner {

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
            admin.setEmail("user@mail.com");
            admin.setActive(true);
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.getAuthority().add(Role.ROLE_ADMIN);
            admin.setStatistics(new UserStatistics());
            admin.setUserAvatar(new UserAvatar());
            userRepository.save(admin);
        }
    }
}