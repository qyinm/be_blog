package qyinm.blog.domain.ArticleTagMap;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import qyinm.blog.domain.Article.Article;
import qyinm.blog.domain.Tag.Tag;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleTagMap {
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Tag tag;

    @Builder
    public ArticleTagMap(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
        article.addArticleTagMap(this);
        tag.addArticleTagMap(this);
    }

    public Tag getTag() {
        return tag;
    }
}
