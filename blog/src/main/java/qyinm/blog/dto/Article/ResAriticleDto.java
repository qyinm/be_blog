package qyinm.blog.dto.Article;

import java.util.List;

import qyinm.blog.dto.Tag.TagDto;

/**
 * ResAritcleDto
 */
public record ResAriticleDto(
        String title,
        String content,
        List<TagDto> tags) {
}