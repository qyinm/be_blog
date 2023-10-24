package qyinm.blog.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import qyinm.blog.dto.UserDto;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void 회원가입_OK() {
        UserDto userDto = new UserDto("abcd", "abcd", "abcd");

        assertThat(userService.signup(userDto).getEmail()).isEqualTo(userDto.getEmail());
    }
}
