package com.training.helpdesk.security.service;

import com.training.helpdesk.dao.UserDAO;
import com.training.helpdesk.model.User;
import com.training.helpdesk.security.model.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    public CustomUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.getByLogin(username);

        return CustomUserDetails.fromUserToCustomUserDetails(user);
    }
}
