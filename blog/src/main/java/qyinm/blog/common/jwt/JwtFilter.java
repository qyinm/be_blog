package qyinm.blog.common.jwt;

import static qyinm.blog.common.constants.jwt.JwtConstants.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import qyinm.blog.common.util.cookie.CookieUtil;
import qyinm.blog.common.util.jwt.TokenProvider;
import qyinm.blog.dto.TokenUserInfo;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;

    private final List<String> jwtIgnoreUrls = List.of(
            "/api/v1/users/test/**",
            "/api/signin",
            "/api/signup",
            "/api/article",
            "/api/v1/users/refresh");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (shouldIgnoreRequest(request)) {
            logger.info("Ignoring request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = getAccessToken(request);

        Authentication authentication = tokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("인증 정보 저장!");

        filterChain.doFilter(request, response);
    }

    private boolean shouldIgnoreRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        Optional<String> judge = jwtIgnoreUrls.stream()
                .filter(v ->
                        Pattern.matches(v.replace("**", ".*"), uri) ||
                                Pattern.matches(v.replace("/**", ""), uri)
                )
                .findFirst();
        return !judge.isEmpty() || "OPTIONS".equals(method);
    }

    private String getAccessToken(HttpServletRequest request) {

        String header = request.getHeader(AUTHORIZATION_HEADER.getValue());
        String token = tokenProvider.resolveToken(header);

        if (token != null && tokenProvider.validateToken(token)) {
            return token;
        }

        token = reissueAccessToken(request);

        return token;
    }

    public String reissueAccessToken(HttpServletRequest request) {
        Cookie refreshTokenCookie = cookieUtil.getCookieFromRequest(request, REFRESH_TOKEN.getValue())
                .orElseThrow(() -> new IllegalArgumentException("Refresh Token not Found"));

        String refreshToken = refreshTokenCookie.getValue();

        // refresh token valid 필요
        if (!tokenProvider.validateToken(refreshToken)) {
            new IllegalArgumentException("refresh token이 잘 못 됐습니다.");
        }
        TokenUserInfo tokenUserInfo = tokenProvider.getUserInfoFromToken(refreshToken);
        return tokenProvider.generateAccessToken(tokenUserInfo);
    }
}