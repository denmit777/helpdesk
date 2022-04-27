package com.training.helpdesk.converter.impl;

import com.training.helpdesk.converter.UserConverter;
import com.training.helpdesk.dto.user.UserDto;
import com.training.helpdesk.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setLogin(user.getEmail());
        userDto.setPassword(user.getPassword());

        return userDto;
    }

    @Override
    public User fromUserDto(UserDto userDto) {
        User user = new User();

        user.setEmail(userDto.getLogin());
        user.setPassword(userDto.getPassword());

        return user;
    }
}
