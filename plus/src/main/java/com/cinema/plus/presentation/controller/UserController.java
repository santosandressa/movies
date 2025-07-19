package com.cinema.plus.presentation.controller;

import com.cinema.plus.application.service.UserService;
import com.cinema.plus.domain.entity.User;
import com.cinema.plus.presentation.dto.request.CreateUserRequestDTO;
import com.cinema.plus.presentation.dto.request.UserDTO;
import com.cinema.plus.presentation.mapper.UserRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserRestMapper mapper;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateUserRequestDTO requestDTO) {

        User user = userService.createUser(mapper.toCreateUserRequest(requestDTO));
        user.setPassword(requestDTO.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toUserDto(user));
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> allUsers = userService.findAllUsers();
        return ResponseEntity.ok(allUsers.stream()
                .map(mapper::toUserDto)
                .toList());
    }

}
