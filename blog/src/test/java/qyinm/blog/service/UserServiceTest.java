package qyinm.blog.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import qyinm.blog.domain.User.UserRepository;
import qyinm.blog.dto.UserDto;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void 회원가입_OK() {
        UserDto userDto = UserDto.builder()
                .email("test@test.com")
                .password("test")
                .build();

        assertThat(userService.signup(userDto).getEmail()).isEqualTo(userDto.getEmail());
    }

    @DisplayName("회원가입 중복 email")
    @Test
    void 회원가입_fail() {
        UserDto userDto = UserDto.builder()
                .email("test@test.com")
                .password("test")
                .build();
        userService.signup(userDto);

        UserDto errDto = UserDto.builder()
                .email("test@test.com")
                .password("error")
                .build();

        assertThatIllegalArgumentException().isThrownBy(() -> userService.signup(errDto));
    }
}
