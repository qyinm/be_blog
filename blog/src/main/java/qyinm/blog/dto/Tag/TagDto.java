package qyinm.blog.dto.Tag;

import lombok.Builder;
import qyinm.blog.domain.Tag.Tag;

/**
 * TagDto
 */ 
@Builder
public record TagDto(
    String tagName
) {
    public Tag toEntity() {
        return Tag.builder()
               .name(tagName)
                .build();
    }
}
