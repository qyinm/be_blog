package qyinm.blog.common.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import qyinm.blog.common.constants.jwt.JwtConstants;
import qyinm.blog.common.util.cookie.CookieUtil;
import qyinm.blog.common.util.jwt.JwtUtil;
import qyinm.blog.common.util.jwt.TokenProvider;
import qyinm.blog.dto.TokenUserInfo;

import static qyinm.blog.common.constants.jwt.JwtConstants.*;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String jwt = getAccessToken(request);
    }

    private String getAccessToken(HttpServletRequest request) {

        String header = request.getHeader(AUTHORIZATION_HEADER.getValue());
        String token = jwtUtil.resolveToken(header);

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

        TokenUserInfo tokenUserInfo = tokenProvider.getUserInfoFromToken(refreshToken);
        return tokenProvider.generateAccessToken(tokenUserInfo);
    }
}