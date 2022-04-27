package com.training.helpdesk.security.controller;

import com.training.helpdesk.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private Role role;
}
