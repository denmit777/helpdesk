package com.training.helpdesk.dao;

import com.training.helpdesk.model.User;

import java.util.List;

public interface UserDAO {

    void save(User user);

    User getById(Long id);

    User getByLogin(String login);

    List<User> getAll();

    List<User> getAllManagers();

    List<User> getAllEngineers();
}

