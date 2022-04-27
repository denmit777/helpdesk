package com.training.helpdesk.service.impl;

import com.training.helpdesk.converter.UserConverter;
import com.training.helpdesk.dao.UserDAO;
import com.training.helpdesk.dto.user.UserDto;
import com.training.helpdesk.model.User;
import com.training.helpdesk.model.enums.Role;
import com.training.helpdesk.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDAO userDAO, UserConverter userConverter, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User save(UserDto userDto) {
        User user = userConverter.fromUserDto(userDto);

        user.setRole(Role.ROLE_EMPLOYEE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDAO.save(user);

        return user;
    }

    @Override
    @Transactional
    public User getByLoginAndPassword(String login, String password) {
        User user = userDAO.getByLogin(login);

        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        throw new UsernameNotFoundException("user not found");
    }
}
