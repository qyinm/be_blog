package qyinm.blog.dto.Article;

import java.util.List;

import lombok.Builder;
import qyinm.blog.domain.Article.Article;
import qyinm.blog.dto.Tag.TagDto;

@Builder
public record ArticleDto(
        String title,
        String content,
        List<TagDto> tags) {

    public Article toArticleEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }

}
