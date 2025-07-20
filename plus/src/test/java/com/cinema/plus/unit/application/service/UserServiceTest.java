package com.cinema.plus.unit.application.service;

import com.cinema.plus.application.service.UserService;
import com.cinema.plus.domain.entity.CreateUserRequest;
import com.cinema.plus.domain.entity.User;
import com.cinema.plus.domain.ports.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .build();

        createUserRequest = new CreateUserRequest();
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        when(userRepository.save(any(CreateUserRequest.class))).thenReturn(testUser);

        User result = userService.createUser(createUserRequest);

        assertThat(result).isEqualTo(testUser);
        verify(userRepository).save(createUserRequest);
    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAllUsers() {
        List<User> expectedUsers = Collections.singletonList(testUser);
        when(userRepository.findAllUsers()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.findAllUsers();

        assertThat(actualUsers).isEqualTo(expectedUsers);
        verify(userRepository).findAllUsers();
    }
}