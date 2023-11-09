package qyinm.blog.common.util.jwt;

import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import qyinm.blog.common.constants.jwt.JwtProperties;
import qyinm.blog.dto.TokenUserInfo;
import qyinm.blog.service.UserService;

import static qyinm.blog.common.constants.jwt.JwtConstants.*;

@RequiredArgsConstructor
@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private static final String USER_ID = "userId";
    private Key key;
    private final UserService userService;
    private final JwtProperties jwtProperties;

    // 빈이 생성되고 주입을 받은 후에 secret값을 Base64 Decode해서 key 변수에 할당하기 위해
    @Override
    public void afterPropertiesSet() throws Exception {

        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(TokenUserInfo tokenUserInfo) {

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setClaims(createClaims(tokenUserInfo))
                .setExpiration(getExpireDate(Duration.ofMinutes(30)))
                .compact();
    }

    public String generateRefreshToken(TokenUserInfo tokenUserInfo) {

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setClaims(createClaims(tokenUserInfo))
                .setExpiration(getExpireDate(Duration.ofDays(2)))
                .compact();
    }

    public String resolveToken(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(TOKEN_PREFIX.getValue())) {
            return authHeader.substring(TOKEN_PREFIX.getValue().length());
        }
        return "";
    }

    public Map<String, Object> createClaims(TokenUserInfo dto) {
        return Map.of(USER_ID, dto.userId(),
                AUTHORITIES_KEY, dto.authorities()
        );
    }

    public TokenUserInfo getUserInfoFromToken(String token) {
        Claims claims = getClaimsFromToken(token);

        return TokenUserInfo.builder()
                .userId(claims.get(USER_ID, Long.class))
                .authorities(claims.get(AUTHORITIES_KEY, String.class))
                .build();
    }

    public Authentication getAuthentication(String token) {

        Claims claims = getClaimsFromToken(token);

        Long getUserIdFromClaims = claims.get(USER_ID, Long.class);

        String email = userService.getUserEmailByUserId(getUserIdFromClaims);

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User principal = new User(email, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private Date getExpireDate(Duration duration) {
        long now = (new Date()).getTime();

        return new Date(now + duration.toMillis());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
    }
}
