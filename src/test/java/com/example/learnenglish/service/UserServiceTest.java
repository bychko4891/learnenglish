package com.example.learnenglish.service;

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.model.users.UserGender;
import com.example.learnenglish.repository.UserRepository;
import com.example.learnenglish.responsemessage.Message;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Disabled
    void userProfileDelete() {
    }

    @Test
    @Disabled
    void getUsersPage() {
    }

    @Test
    @Disabled
    void userActiveEditAdminPage() {
    }

    @Test
    @Disabled
    void saveUserIp() {
    }

    @Test
    @Disabled
    void generatePassword() {
    }

    @Test
    @Disabled
    void activateUser() {
    }

    @Test
    @Disabled
    void setUserTextInLesson() {
    }
}