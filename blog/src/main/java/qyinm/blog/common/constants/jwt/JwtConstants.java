package qyinm.blog.common.constants.jwt;

import lombok.Getter;

@Getter
public enum JwtConstants {
    AUTHORIZATION_HEADER("Authorization"),
    TOKEN_PREFIX("Bearer "),
    ACCESS_TOKEN("accessToken"), REFRESH_TOKEN("refreshToken");

    private final String value;

    JwtConstants(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("AuthConstants(value=%s)", this.value);
    }
}
