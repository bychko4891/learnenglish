package com.example.learnenglish.utils;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Image;
import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.model.WayForPayModule;
import com.example.learnenglish.model.users.*;
import com.example.learnenglish.repository.UserRepository;
import com.example.learnenglish.service.PageApplicationService;
import com.example.learnenglish.service.WayForPayModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements ApplicationRunner {
    @Value(("${user.admin.email}"))
    private String adminEmail;

    @Value(("${user.admin.password}"))
    private String adminPassword;


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PageApplicationService pageApplicationService;

    private final WayForPayModuleService wayForPayModuleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setEmail(adminEmail);
            admin.setActive(true);
            admin.setUserPhrasesInLesson(false);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.getAuthority().add(Role.ROLE_ADMIN);
            admin.getGender().add(UserGender.MALE);
            UserStatistics userStatistics = new UserStatistics();
            userStatistics.setStudyTimeInTwoWeeks(new ArrayList<>(Arrays.asList(0)));
            userStatistics.setTrainingDaysInMonth(new ArrayList<>(Arrays.asList(LocalDate.now())));
            admin.setStatistics(userStatistics);
            admin.setUserAvatar(new Image());
            userRepository.save(admin);
            createUserDemo();
            createPageApplication();
            createWayForPayModule();
        }
    }

    public void createUserDemo() {
        User demo = new User();
        demo.setFirstName("Demo");
        demo.setLastName("Demo");
        demo.setEmail("demo@mail.com");
        demo.setActive(true);
        demo.setUserPhrasesInLesson(false);
        demo.setPassword(passwordEncoder.encode("demo"));
        demo.getAuthority().add(Role.ROLE_DEMO);
        demo.getGender().add(UserGender.MALE);
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setStudyTimeInTwoWeeks(new ArrayList<>(Arrays.asList(0)));
        userStatistics.setTrainingDaysInMonth(new ArrayList<>(Arrays.asList(LocalDate.now())));
        demo.setStatistics(userStatistics);
        demo.setUserAvatar(new Image());
        userRepository.save(demo);

    }

    private void createPageApplication() {
        PageApplication pageApplicationLogin = new PageApplication("Login page");
        pageApplicationService.savePageApplication(pageApplicationLogin);
        PageApplication pageApplicationRegistration = new PageApplication("Registration page");
        pageApplicationService.savePageApplication(pageApplicationRegistration);
        PageApplication pageApplicationAboutTheApp = new PageApplication("About the app page");
        pageApplicationService.savePageApplication(pageApplicationAboutTheApp);
        PageApplication pageApplicationUploadUserText = new PageApplication("Mini description phrase lessons");
        pageApplicationService.savePageApplication(pageApplicationUploadUserText);
        PageApplication pageApplicationMainTop = new PageApplication("Main page Top");
        pageApplicationService.savePageApplication(pageApplicationMainTop);
        PageApplication pageApplicationMainBottom = new PageApplication("Main page Bottom");
        pageApplicationService.savePageApplication(pageApplicationMainBottom);

    }
    private void createWayForPayModule() {
        WayForPayModule wayForPayModule = new WayForPayModule();
        wayForPayModule.setMerchantAccount("Enter merch");
        wayForPayModule.setMerchantSecretKey("Enter key");
        wayForPayModuleService.saveWayForPayModule(wayForPayModule);
    }
}
