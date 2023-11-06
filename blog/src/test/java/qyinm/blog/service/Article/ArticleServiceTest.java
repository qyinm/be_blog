package qyinm.blog.service.Article;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import qyinm.blog.domain.Article.Article;
import qyinm.blog.domain.Article.ArticleRepository;
import qyinm.blog.dto.Article.ArticleDto;
import qyinm.blog.dto.Tag.TagDto;

@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @BeforeEach
    void cleanup() {
        articleRepository.deleteAll();
    }

    @Test
    void create_success() {
        List<TagDto> tags = List.of(
            new TagDto("tag1"),
            new TagDto("tag2")
        );

        ArticleDto reqDto = ArticleDto.builder()
                .title("test")
                .content("<h1>123</h1>")
                .tags(tags)
                .build();
        
        Article created = articleService.createArticle(reqDto);
        assertThat(created.getTitle()).isEqualTo("test");
        assertThat(created.getTagDtos().size()).isEqualTo(2);
    }
}
