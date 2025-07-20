package com.cinema.plus.unit.presentation.controller;

import com.cinema.plus.application.service.UserService;
import com.cinema.plus.domain.entity.CreateUserRequest;
import com.cinema.plus.domain.entity.User;
import com.cinema.plus.presentation.controller.UserController;
import com.cinema.plus.presentation.dto.request.CreateUserRequestDTO;
import com.cinema.plus.presentation.dto.request.UserDTO;
import com.cinema.plus.presentation.mapper.UserRestMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRestMapper mapper;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void shouldCreateUserSuccessfully() {
        CreateUserRequestDTO requestDTO = new CreateUserRequestDTO("test@email.com", "user_test", "password123");
        CreateUserRequest request = new CreateUserRequest("test@email.com", "user_test", "password123");

        User createdUser = User.builder()
                .id(1L)
                .email("test@email.com")
                .username("user_test")
                .password("password123")
                .build();

        UserDTO expectedDTO = new UserDTO("user_test", "test@email.com");

        when(mapper.toCreateUserRequest(requestDTO)).thenReturn(request);
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(createdUser);
        when(mapper.toUserDto(createdUser)).thenReturn(expectedDTO);

        ResponseEntity<UserDTO> response = userController.createUser(requestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());
    }

    @Test
    @DisplayName("Deve retornar lista de usuários com sucesso")
    void shouldGetAllUsersSuccessfully() {

        User createdUser = User.builder()
                .id(1L)
                .email("test@email.com")
                .username("user_test")
                .password("password123")
                .build();

        User createdUser2 = User.builder()
                .id(2L)
                .email("john@emaill.com")
                .username("john_doe")
                .password("password456")
                .build();

        List<User> existingUsers = List.of(
                createdUser,
                createdUser2
        );

        List<UserDTO> expectedDTOs = List.of(
                new UserDTO("user_test", "test@email.com"),
                new UserDTO("john_doe", "john@email.com")
        );

        when(userService.findAllUsers()).thenReturn(existingUsers);
        when(mapper.toUserDto(existingUsers.get(0))).thenReturn(expectedDTOs.get(0));
        when(mapper.toUserDto(existingUsers.get(1))).thenReturn(expectedDTOs.get(1));

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedDTOs, response.getBody());
    }
}