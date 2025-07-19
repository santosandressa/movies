package com.cinema.plus.infrastructure.persistence.repository.impl;

import com.cinema.plus.domain.entity.CreateUserRequest;
import com.cinema.plus.domain.entity.User;
import com.cinema.plus.domain.ports.repository.UserRepository;
import com.cinema.plus.infrastructure.entity.UserEntity;
import com.cinema.plus.infrastructure.mapper.UserMapper;
import com.cinema.plus.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper mapper;


    @Override
    public User save(CreateUserRequest user) {
        if (userJpaRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


        var entity = UserEntity.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(encoder.encode(user.getPassword()))
                .build();

        return mapper.toUser(userJpaRepository.save(entity));
    }


    @Override
    public List<User> findAllUsers() {
        return userJpaRepository.findAll().stream()
                .map(mapper::toUser)
                .toList();
    }
}
