package qyinm.blog.service.Tag;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import qyinm.blog.domain.Tag.Tag;
import qyinm.blog.domain.Tag.TagRepository;
import qyinm.blog.dto.Tag.TagDto;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public Optional<Tag> findByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    public Tag save(TagDto tagDto) {
        return tagRepository.save(tagDto.toEntity());
    }

    public Long countUsingTag(TagDto tagDto) {
        Tag tag = findByName(tagDto.tagName())
                .orElseThrow(() -> new IllegalArgumentException("illegal tagname"));

        return tag.getArticlTagMapsCount();
    }
}
