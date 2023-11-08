package qyinm.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import qyinm.blog.common.util.jwt.TokenProvider;
import qyinm.blog.domain.User.User;
import qyinm.blog.dto.TokenDto;
import qyinm.blog.dto.TokenUserInfo;
import qyinm.blog.dto.UserDto;
import qyinm.blog.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody UserDto userDto) {
        User user = userService.signin(userDto);

        TokenUserInfo tokenUserInfo = TokenUserInfo.builder()
                .userEmail(user.getEmail())
                .authorities(user.getRole())
                .build();

        String accessToken = tokenProvider.generateAccessToken(tokenUserInfo);
        String refreshToken = tokenProvider.generateRefreshToken(tokenUserInfo);

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.ok(tokenDto);
    }
}
