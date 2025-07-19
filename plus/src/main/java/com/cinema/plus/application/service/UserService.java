package com.cinema.plus.application.service;

import com.cinema.plus.domain.entity.CreateUserRequest;
import com.cinema.plus.domain.entity.User;
import com.cinema.plus.domain.ports.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public User createUser(CreateUserRequest user) {
        log.info("Adding user with username: {}", user.getUsername());
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

}
