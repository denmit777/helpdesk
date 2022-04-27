package com.training.helpdesk.converter;

import com.training.helpdesk.dto.user.UserDto;
import com.training.helpdesk.model.User;

public interface UserConverter {

    UserDto convertToUserDto(User user);

    User fromUserDto(UserDto userDto);
}
