package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.model.users.UserGender;
import com.example.learnenglish.repository.UserRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MailSenderInApp mailSender;
    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, mailSender, request);
    }

    @Test
    void shouldCreateUser() {
        var user = new User();
        user.setFirstName("John");
        user.setId(1L);
        user.setLastName("Morris");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        boolean res = userService.createUser(user);

        assertTrue(res);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldFindByEmail() {
        var userEmail = "johnmorris@gmail.com";
        var user = new User();
        user.setEmail(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        var foundUser = userService.findByEmail(userEmail);

        assertNotNull(foundUser);
        assertEquals(userEmail, foundUser.getEmail());
    }

    @Test
    void shouldFindByIdIfUserExist() {
        var userId = 1L;
        var user = new User();
        user.setId(userId);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var actualUser = userService.findById(userId);

        assertNotNull(actualUser);
        assertEquals(userId, actualUser.getId());
    }

    @Test
    void findByIdIfUserNotExist() {
        var userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.findById(userId));
    }

    @Test
    void shouldUpdateUserInfoIfUserExist() {
        var userId = 1L;
        var userName = "John";
        var userSurname = "Morris";
        var gender = "MALE";
        var user = new User();
        user.setId(userId);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.updateUserInfo(userId, userName, userSurname, gender);

        verify(userRepository.save(captor.capture()));

        var updatedUser = captor.getValue();

        assertEquals(userName, updatedUser.getFirstName());
        assertEquals(userSurname, updatedUser.getLastName());

        assertTrue(updatedUser.getGender().contains(UserGender.MALE));
    }

    @Test
    void updateUserInfoIfUserNotExist() {
        var userId = 1L;
        var userName = "John";
        var userSurname = "Morris";
        var gender = "MALE";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateUserInfo(userId, userName, userSurname, gender));
    }

    @Test
    void updateUserPasswordIfMatches() {
        var userId = 1L;
        var oldPassword = "oldPassword";
        var newPassword = "newPassword";
        var encodedPassword = "encodedPassword";

        var user = new User();
        user.setId(userId);
        user.setPassword(encodedPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, encodedPassword)).thenReturn(true);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        var responseMessage = userService.updateUserPassword(userId, oldPassword, newPassword);
        verify(userRepository.save(captor.capture()));

        var updatedUser = captor.getValue();

        assertEquals(newPassword, updatedUser.getPassword());
        assertEquals(Message.SUCCESS_UPDATEPASSWORD, responseMessage.getMessage());
    }

    @Test
    void userProfileDelete() {
        var userPassword = "userPassword";
        var encodedPassword = "encodedPassword";

        var user = new User();
        user.setPassword(encodedPassword);

        when(passwordEncoder.matches(userPassword, encodedPassword)).thenReturn(true);

        var responseMessage = userService.userProfileDelete(user, encodedPassword);

        verify(userRepository).delete(user);
        verify(request).getSession(false);

        assertEquals(Message.SUCCESS_UPDATEPASSWORD, responseMessage.getMessage());
    }

    @Test
    void getUsersPage() {
        var page = 10;
        var size = 10;
        var pageable = PageRequest.of(page, size);

        List<User> userList = new ArrayList<>();

        userList.add(new User());
        userList.add(new User());

        Page<User> expectedPage = new PageImpl<>(userList, pageable, userList.size());
        when(userRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<User> resultPage = userService.getUsersPage(page, size);

        assertEquals(expectedPage, resultPage);
    }

    @Test
    void userActiveEditAdminPage() {
        var userId = 1L;
        var userActive = true;
        var user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.userActiveEditAdminPage(userId, userActive);

        assertTrue(user.isActive());
        verify(userRepository.save(user));
    }

    @Test
    void saveUserIp() {
        var userId = 1L;
        var userIP = "192.168.1.1";
        var user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.saveUserIp(userId, userIP);

        assertEquals(userIP, user.getUserIp());
        verify(userRepository).save(user);
    }

    @Test
    void generatePassword() {
        var email = "user@example.com";
        var password = "password";
        var user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        ResponseMessage responseMessage = userService.generatePassword(email);

        assertEquals(Message.SUCCESS_FORGOT_PASSWORD, responseMessage.getMessage());
    }

    @Test
    void activateUser() {
        var user = new User();
        var code = "code";
        user.setActive(true);
        user.setActivationCode(code);

        when(userRepository.findByActivationCode(code)).thenReturn(user);
        var result = userService.activateUser(code);

        assertTrue(result);
        assertTrue(user.isActive());
        assertNull(user.getActivationCode());
        verify(userRepository).save(user);


    }

    @Test
    void setUserTextInLesson() {
        var user = new User();
        var userId = 1L;
        var isCheck = true;
        user.setUserTextInLesson(isCheck);
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        var responceMessage = userService.setUserTextInLesson(userId, isCheck);

        assertEquals(Message.SUCCESS_CHECKBOX, responceMessage.getMessage());
        assertTrue(user.isUserTextInLesson());
        verify(userRepository).save(user);
    }
}