package qyinm.blog.dto;

import lombok.Builder;

/**
 * TokenDto
 */
@Builder
public record TokenDto(
    String accessToken,
    String refreshToken
) {

}