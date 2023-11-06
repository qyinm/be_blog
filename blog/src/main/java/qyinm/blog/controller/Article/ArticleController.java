package qyinm.blog.controller.Article;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import qyinm.blog.domain.Article.Article;
import qyinm.blog.dto.Article.ArticleDto;
import qyinm.blog.service.Article.ArticleService;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/article")
    public ResponseEntity<ArticleDto> postArticle(@RequestBody ArticleDto articleDto) {
        Article saved = articleService.createArticle(articleDto);
        ArticleDto res = ArticleDto.builder()
                .title(saved.getTitle())
                .content(saved.getContent())
                .tags(saved.getTagDtos())
                .build();

        return ResponseEntity.ok(res);
    }
}
