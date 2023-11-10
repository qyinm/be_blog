package qyinm.blog.controller.Article;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static qyinm.blog.common.constants.jwt.JwtConstants.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import qyinm.blog.common.util.jwt.TokenProvider;
import qyinm.blog.domain.Article.ArticleRepository;
import qyinm.blog.domain.User.Role;
import qyinm.blog.domain.User.User;
import qyinm.blog.domain.User.UserRepository;
import qyinm.blog.dto.TokenUserInfo;
import qyinm.blog.dto.Article.ArticleDto;
import qyinm.blog.dto.Tag.TagDto;
import qyinm.blog.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TokenProvider tokenProvider;

    String accessToken;
    String refreshToken;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void getAccessToken() {
        // when(userService.getUserByEmail("test@test.com"))
        //         .thenReturn(User.builder()
        //                 .id(1L)
        //                 .email("test@test.com")
        //                 .password("")
        //                 .role(Role.USER)
        //                 .build());
        // when(userService.getUserEmailByUserId(1L)).thenReturn("test@test.com");
        userRepository.save(User.builder()
                         .id(1L)
                         .email("test@test.com")
                         .password("")
                         .role(Role.USER)
                         .build());
        TokenUserInfo tokenUserInfo = TokenUserInfo.builder()
                .userId(1L)
                .authorities("USER")
                .build();
        accessToken = tokenProvider.generateAccessToken(tokenUserInfo);
        refreshToken = tokenProvider.generateRefreshToken(tokenUserInfo);
    }

    @AfterEach
    void cleanup() {
        articleRepository.deleteAll();
    }

    @DisplayName("유효한 accessToken 있는 상태에서 create")
    @Test
    void create_success() throws Exception {
        String url = "/api/article";

        List<TagDto> tags = List.of(
                new TagDto("tag1"),
                new TagDto("tag2"));

        ArticleDto reqDto = ArticleDto.builder()
                .title("test")
                .content("<h1>123</h1>")
                .tags(tags)
                .build();
        String body = objectMapper.writeValueAsString(reqDto);
        System.out.println(body);

        ResultActions result = mockMvc.perform(post(url)
                .header(AUTHORIZATION_HEADER.getValue(), TOKEN_PREFIX.getValue() + accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.tags.length()").value(tags.size()))
                .andDo(print());
    }

    @DisplayName("access token 없이 article 생성")
    @Test
    void create_err() throws Exception {
        String url = "/api/article";

        List<TagDto> tags = List.of(
                new TagDto("tag1"),
                new TagDto("tag2"));

        ArticleDto reqDto = ArticleDto.builder()
                .title("test")
                .content("<h1>123</h1>")
                .tags(tags)
                .build();
        String body = objectMapper.writeValueAsString(reqDto);
        System.out.println(body);

        assertThatIllegalArgumentException().isThrownBy(() -> mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
                .andDo(print()));
    }

    @DisplayName("access token 없이 refresh token으로 access token 재발급해서 article 생성")
    @Test
    void create_reissue() throws Exception {
        String url = "/api/article";

        List<TagDto> tags = List.of(
                new TagDto("tag1"),
                new TagDto("tag2"));

        ArticleDto reqDto = ArticleDto.builder()
                .title("test")
                .content("<h1>123</h1>")
                .tags(tags)
                .build();
        String body = objectMapper.writeValueAsString(reqDto);
        System.out.println(body);

        ResultActions result = mockMvc.perform(post(url)
                .header(AUTHORIZATION_HEADER.getValue(), TOKEN_PREFIX.getValue() + "")
                .cookie(new Cookie(REFRESH_TOKEN.getValue(), refreshToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.tags.length()").value(tags.size()))
                .andDo(print());
    }

    @DisplayName("access token 없이 잘못된 refresh token으로 생성 시도, 에러남.")
    @Test
    void create_reissue_err() throws Exception {
        String url = "/api/article";

        List<TagDto> tags = List.of(
                new TagDto("tag1"),
                new TagDto("tag2"));

        ArticleDto reqDto = ArticleDto.builder()
                .title("test")
                .content("<h1>123</h1>")
                .tags(tags)
                .build();
        String body = objectMapper.writeValueAsString(reqDto);
        System.out.println(body);

        assertThatThrownBy(() -> mockMvc.perform(post(url)
                .header(AUTHORIZATION_HEADER.getValue(), TOKEN_PREFIX.getValue() + "")
                .cookie(new Cookie(REFRESH_TOKEN.getValue(), refreshToken + "asdf"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
                .andDo(print())).isInstanceOf(io.jsonwebtoken.security.SecurityException.class);
    }
}
