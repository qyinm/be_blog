package qyinm.blog.service.Article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import qyinm.blog.domain.Article.Article;
import qyinm.blog.domain.Article.ArticleRepository;
import qyinm.blog.dto.Article.ArticleDto;
import qyinm.blog.service.ArticlaTagMap.ArticleTagMapService;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleTagMapService articleTagMapService;

    @Transactional
    public Article createArticle(ArticleDto dto) {
        Article article = articleRepository.save(dto.toArticleEntity());

        if (dto.tags() != null) {
            articleTagMapService.saveArticlTag(article, dto.tags());
        }

        return article;
    }
}
