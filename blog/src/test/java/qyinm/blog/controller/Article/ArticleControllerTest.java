package qyinm.blog.controller.Article;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import qyinm.blog.domain.Article.ArticleRepository;
import qyinm.blog.dto.Article.ArticleDto;
import qyinm.blog.dto.Tag.TagDto;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void cleanup() {
        articleRepository.deleteAll();
    }

    @Test
    void create_success() throws Exception {
        String url = "/api/article";
        
        List<TagDto> tags = List.of(
            new TagDto("tag1"),
            new TagDto("tag2")
        );

        ArticleDto reqDto = ArticleDto.builder()
                .title("test")
                .content("<h1>123</h1>")
                .tags(tags)
                .build();
        String body = objectMapper.writeValueAsString(reqDto);
        System.out.println(body);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body));
        
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("test"))
            .andExpect(jsonPath("$.tags.length()").value(tags.size()));
    }
}
