package com.training.helpdesk.security.controller;

import com.training.helpdesk.dto.user.UserDto;
import com.training.helpdesk.model.User;
import com.training.helpdesk.security.config.jwt.JwtTokenProvider;
import com.training.helpdesk.service.UserService;
import com.training.helpdesk.service.ValidationService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ValidationService validationService;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider, ValidationService validationService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.validationService = validationService;
    }

    @PostMapping("/")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto userDto,
                                          BindingResult bindingResult) {
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }

        User savedUser = userService.save(userDto);

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedUserLocation = currentUri + "/" + savedUser.getId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, savedUserLocation)
                .body(savedUser);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticationUser(@RequestBody @Valid UserDto userDto,
                                  BindingResult bindingResult) {
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }

        User user = userService.getByLoginAndPassword(userDto.getLogin(), userDto.getPassword());

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole());

        return new ResponseEntity<>(new AuthResponse(token, user.getRole()), HttpStatus.OK);
    }

    private boolean checkErrors(List<String> errorMessage) {
        return !errorMessage.isEmpty();
    }
}
