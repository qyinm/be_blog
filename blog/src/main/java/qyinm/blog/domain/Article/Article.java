package qyinm.blog.domain.Article;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import qyinm.blog.domain.ArticleTagMap.ArticleTagMap;
import qyinm.blog.domain.User.User;
import qyinm.blog.dto.Tag.TagDto;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String title;

    private String content;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "article", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private List<ArticleTagMap> articleTagMaps = new ArrayList<>();

    @Builder
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addArticleTagMap(ArticleTagMap articleTagMap) {
        articleTagMaps.add(articleTagMap);
    }

    public List<TagDto> getTagDtos() {
        return articleTagMaps.stream()
                .map(articleTagMap -> 
                    articleTagMap.getTag().toDto()
                )
                .toList();
    }

    public Article addUser(User user) {
        this.user = user;
        user.addArticle(this);
        return this;
    }
}
