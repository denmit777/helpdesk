package com.training.helpdesk.dao.impl;

import com.training.helpdesk.model.User;
import com.training.helpdesk.dao.UserDAO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private static final String QUERY_SELECT_FROM_USER = "from User";
    private static final String QUERY_SELECT_ALL_MANAGERS_FROM_USERS = "from User u where u.role = 'ROLE_MANAGER'";
    private static final String QUERY_SELECT_ALL_ENGINEERS_FROM_USERS = "from User u where u.role = 'ROLE_ENGINEER'";

    @PersistenceContext
    private final EntityManager entityManager;

    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getByLogin(String login) {
        return getAll().stream()
                .filter(user -> login.equals(user.getEmail()))
                .findAny()
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    @Override
    public List<User> getAll() {
        return entityManager.createQuery(QUERY_SELECT_FROM_USER).getResultList();
    }

    @Override
    public List<User> getAllManagers() {
        return entityManager.createQuery(QUERY_SELECT_ALL_MANAGERS_FROM_USERS, User.class)
                .getResultList();
    }

    @Override
    public List<User> getAllEngineers() {
        return entityManager.createQuery(QUERY_SELECT_ALL_ENGINEERS_FROM_USERS, User.class)
                .getResultList();
    }
}
