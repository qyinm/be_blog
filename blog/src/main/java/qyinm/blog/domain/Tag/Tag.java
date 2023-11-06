package qyinm.blog.domain.Tag;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import qyinm.blog.domain.ArticleTagMap.ArticleTagMap;
import qyinm.blog.dto.Tag.TagDto;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tag {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "tag")
    private List<ArticleTagMap> articleTagMaps = new ArrayList<>();

    @Builder
    public Tag(String name) {
        this.name = name;
    }

    public void addArticleTagMap(ArticleTagMap articleTagMap) {
        articleTagMaps.add(articleTagMap);
    }

    public TagDto toDto() {
        return TagDto.builder()
                .tagName(name)
                .build();
    }

    public Long getArticlTagMapsCount() {
        return articleTagMaps.stream().count();
    }
}
