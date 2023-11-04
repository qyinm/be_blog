package qyinm.blog.dto;

import java.util.Map;

import lombok.Builder;


/**
 * TokenUserInfo
 */
@Builder
public record TokenUserInfo(
    String userEmail,
    String authorities) {
    
    public Map<String, String> toClaims() {
        return Map.of(
            "user_email", userEmail,
            "authorities", authorities
        );
    }
}