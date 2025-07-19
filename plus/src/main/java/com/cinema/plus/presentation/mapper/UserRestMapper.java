package com.cinema.plus.presentation.mapper;

import com.cinema.plus.domain.entity.CreateUserRequest;
import com.cinema.plus.domain.entity.User;
import com.cinema.plus.presentation.dto.request.CreateUserRequestDTO;
import com.cinema.plus.presentation.dto.request.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRestMapper {

    @Mapping(source = "password", target = "password")
    CreateUserRequest toCreateUserRequest(CreateUserRequestDTO requestDTO);

    UserDTO toUserDto(User user);
}
