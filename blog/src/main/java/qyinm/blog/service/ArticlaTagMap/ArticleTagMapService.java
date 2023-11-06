package qyinm.blog.service.ArticlaTagMap;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import qyinm.blog.domain.Article.Article;
import qyinm.blog.domain.ArticleTagMap.ArticleTagMap;
import qyinm.blog.domain.ArticleTagMap.ArticleTagMapRepository;
import qyinm.blog.domain.Tag.Tag;
import qyinm.blog.dto.Tag.TagDto;
import qyinm.blog.service.Tag.TagService;

@RequiredArgsConstructor
@Service
public class ArticleTagMapService {

    private final TagService tagService;
    private final ArticleTagMapRepository articleTagMapRepository;

    public List<ArticleTagMap> saveArticlTag(Article article, List<TagDto> tagDtos) {
        List<Tag> tags = tagDtos.stream()
                .map(tagDto -> tagService.findByName(tagDto.tagName())
                        .orElseGet(() -> tagService.save(tagDto)))
                .toList();

        return tags.stream()
                .map(tag -> articleTagMapRepository.save(
                        ArticleTagMap.builder()
                                .article(article)
                                .tag(tag)
                                .build()))
                .toList();
    }
}
