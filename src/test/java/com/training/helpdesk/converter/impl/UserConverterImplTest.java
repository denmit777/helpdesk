package com.training.helpdesk.converter.impl;

import com.training.helpdesk.dto.user.UserDto;
import com.training.helpdesk.model.User;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

public class UserConverterImplTest {

    private UserConverterImpl userConverter;

    @Before
    public void setUp() throws ParseException {
        userConverter = new UserConverterImpl();
    }

    @Test
    public void convertUserToUserDtoTest() {
        User user = new User();

        user.setEmail("user1_mogilev@yopmail.com");
        user.setPassword("P@ssword1");

        UserDto userDto = userConverter.convertToUserDto(user);

        assertEquals(user.getEmail(), userDto.getLogin());
        assertEquals(user.getPassword(), userDto.getPassword());
    }

    @Test
    public void convertUserFromUserDtoTest() {
        User user = userConverter.fromUserDto(new UserDto() {{
            setLogin("user1_mogilev@yopmail.com");
            setPassword("P@ssword1");
        }});

        assertEquals("user1_mogilev@yopmail.com", user.getEmail());
        assertEquals("P@ssword1", user.getPassword());
    }
}