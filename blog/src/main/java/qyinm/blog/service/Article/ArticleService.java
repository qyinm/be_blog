package qyinm.blog.service.Article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import qyinm.blog.common.util.auth.AuthUtils;
import qyinm.blog.domain.Article.Article;
import qyinm.blog.domain.Article.ArticleRepository;
import qyinm.blog.domain.User.User;
import qyinm.blog.dto.Article.ArticleDto;
import qyinm.blog.service.ArticlaTagMap.ArticleTagMapService;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleTagMapService articleTagMapService;
    private final AuthUtils authUtils;

    @Transactional
    public Article createArticle(ArticleDto dto) {
        User securityHolderUser = authUtils.getUserIdInSecurityContextHolder();
        Article article = articleRepository.save(dto.toArticleEntity().addUser(securityHolderUser));

        if (dto.tags() != null) {
            articleTagMapService.saveArticlTag(article, dto.tags());
        }

        return article;
    }
}
