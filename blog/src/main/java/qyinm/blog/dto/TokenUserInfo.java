package qyinm.blog.dto;

import lombok.Builder;

/**
 * TokenUserInfo
 */
@Builder
public record TokenUserInfo(
        Long userId,
        String authorities) {
}