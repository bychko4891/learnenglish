package com.example.learnenglish.service;

import com.example.learnenglish.model.UserContextHolder;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private UserRepository userRepositoryMock;
    @Mock
    private UserContextHolder userContextHolderMock;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsServiceImpl = new UserDetailsServiceImpl(userRepositoryMock, userContextHolderMock);
    }

    @Test
    void shouldLoadUserByUsernameIfExist() {
        var user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setActive(true);
        user.setPassword("password");

        when(userRepositoryMock.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        var userDetails = userDetailsServiceImpl.loadUserByUsername("user@example.com");

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.isEnabled());

        verify(userContextHolderMock, times(1)).setActive(true);
        verify(userRepositoryMock, times(1)).findByEmail("user@example.com");
        verifyNoMoreInteractions(userRepositoryMock);

    }

    @Test
    void shouldGiveExceptionUserByUsernameIfNotExist() {
        when(userRepositoryMock.findByEmail("user@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername("user@example.com");
        });

        verify(userRepositoryMock, times(1)).findByEmail("user@example.com");
        verifyNoMoreInteractions(userRepositoryMock);
    }
}