package com.steiner.vblog;

import com.steiner.vblog.dto.query.TagQuery;
import com.steiner.vblog.dto.request.PostArticleRequest;
import com.steiner.vblog.mapper.ArticleMapper;
import com.steiner.vblog.mapper.TagMapper;
import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.article.ArticleStatus;
import com.steiner.vblog.table_metadata.ArticlesMetadata;
import com.steiner.vblog.table_metadata.TagsMetadata;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@SpringBootTest
public class ArticleMapperTest {
    static Pattern tagP = Pattern.compile("<p\\s+.*?>");
    static Pattern tagBr = Pattern.compile("<br\\s*/?>");
    static Pattern tagOther = Pattern.compile("<.*?>");

    @Resource
    ArticlesMetadata metadata;

    @Resource
    ArticleMapper mapper;

    @Resource
    TagsMetadata tagsMetadata;

    @Resource
    TagMapper tagMapper;

    @Test
    @Transactional
    public void testInsert() {
        TagQuery tagQuery = TagQuery.builder()
                .userId(1)
                .build();

        List<Tag> tags = tagMapper.findAll(tagsMetadata, tagQuery);
        PostArticleRequest request = new PostArticleRequest(
                "hello",
                "# Hello World",
                "<h1> Hello World </h1>",
                null,
                tags,
                ArticleStatus.Published,
                1
        );

        request.htmlContent = sanitizeHtml(request.htmlContent);
        request.summary = generateSummary(request.htmlContent);

        int result = mapper.insertOne(metadata, request);
        System.out.println(result);

        Objects.requireNonNull(request.returningId);
        mapper.findOne(metadata, request.returningId).ifPresent(System.out::println);
    }

    @Test
    public void testFindAllTags() {
        TagQuery tagQuery = TagQuery.builder()
                .userId(1)
                .build();

        List<Tag> tags = tagMapper.findAll(tagsMetadata, tagQuery);
        tags.forEach(tag -> {
            System.out.println("id=%s, name=%s".formatted(tag.id, tag.name));
        });
    }

    @Nonnull
    private String sanitizeHtml(@Nonnull String unsafeHtml) {
        Safelist safelist = Safelist.relaxed()
                .removeTags("script")
                .removeTags("style");

        return Jsoup.clean(unsafeHtml, safelist);
    }

    @Nonnull
    private String stripHtml(@Nonnull String html) {
        String replacement = tagP.matcher(html)
                .replaceAll(match -> "");

        replacement = tagBr.matcher(replacement)
                .replaceAll(match -> "");

        replacement = tagOther.matcher(replacement)
                .replaceAll(match -> "");

        return replacement;
    }

    @Nonnull
    private String generateSummary(@Nonnull String html) {
        String content = stripHtml(html);
        int length = content.length();

        if (length > metadata.summaryLength) {
            return content.substring(0, metadata.summaryLength);
        } else {
            return content;
        }
    }
}
