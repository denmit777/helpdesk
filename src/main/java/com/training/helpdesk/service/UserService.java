package com.training.helpdesk.service;

import com.training.helpdesk.dto.user.UserDto;
import com.training.helpdesk.model.User;

public interface UserService {

    User save(UserDto userDto);

    User getByLoginAndPassword(String login, String password);
}
