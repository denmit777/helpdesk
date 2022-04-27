package com.training.helpdesk.dto.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RunWith(SpringRunner.class)
public class UserDtoTest {

    private static final String LOGIN = "user1_mogilev@yopmail.com";
    private static final String PASSWORD = "P@ssword1";
    private static final String JSON_TO_DESERIALIZE = "{\"login\": \"" + LOGIN + "\",\"password\": \"" + PASSWORD + "\"}";

    private UserDto userDto;

    @Autowired
    private JacksonTester<UserDto> json;

    @Before
    public void setUp() throws ParseException {
        userDto = new UserDto();

        userDto.setLogin(LOGIN);
        userDto.setPassword(PASSWORD);
    }

    @Test
    public void loginSerializesTest() throws IOException {
        assertThat(this.json.write(userDto))
                .extractingJsonPathStringValue("@.login")
                .isEqualTo(LOGIN);
    }

    @Test
    public void passwordSerializesTest() throws IOException {
        assertThat(this.json.write(userDto))
                .extractingJsonPathStringValue("@.password")
                .isEqualTo(PASSWORD);
    }

    @Test
    public void loginDeserializesTest() throws IOException {
        assertThat(this.json.parseObject(JSON_TO_DESERIALIZE).getLogin()).isEqualTo(LOGIN);
    }

    @Test
    public void passwordDeserializesTest() throws IOException {
        assertThat(this.json.parseObject(JSON_TO_DESERIALIZE).getPassword()).isEqualTo(PASSWORD);
    }
}