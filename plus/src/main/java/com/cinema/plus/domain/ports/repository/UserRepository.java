package com.cinema.plus.domain.ports.repository;

import com.cinema.plus.domain.entity.CreateUserRequest;
import com.cinema.plus.domain.entity.User;

import java.util.List;

public interface UserRepository {
    User save(CreateUserRequest user);

    List<User> findAllUsers();
}
