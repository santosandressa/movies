package com.cinema.plus.infrastructure.mapper;

import com.cinema.plus.domain.entity.User;
import com.cinema.plus.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toUserEntity(User user);

    User toUser(UserEntity userEntity);
}
