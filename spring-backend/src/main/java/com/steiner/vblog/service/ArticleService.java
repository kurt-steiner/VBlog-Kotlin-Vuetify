package com.steiner.vblog.service;

import com.steiner.vblog.dto.query.ArticleQuery;
import com.steiner.vblog.dto.request.PostArticleRequest;
import com.steiner.vblog.dto.request.PutArticleRequest;
import com.steiner.vblog.exception.ServerInternalException;
import com.steiner.vblog.mapper.ArticleMapper;
import com.steiner.vblog.model.article.Article;
import com.steiner.vblog.model.article.ArticleShortcut;
import com.steiner.vblog.table_metadata.ArticlesMetadata;
import com.steiner.vblog.util.Page;
import com.steiner.vblog.validate.impl.PostArticleRequestValidator;
import com.steiner.vblog.validate.impl.PutArticleRequestValidator;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class ArticleService {
    static Pattern tagP = Pattern.compile("<p\\s+.*?>");
    static Pattern tagBr = Pattern.compile("<br\\s*/?>");
    static Pattern tagOther = Pattern.compile("<.*?>");

    @Resource
    ArticlesMetadata metadata;

    @Resource
    ArticleMapper mapper;

    @Resource
    PostArticleRequestValidator postValidator;

    @Resource
    PutArticleRequestValidator putValidator;

    @Nonnull
    public Article insertOne(@Nonnull PostArticleRequest request) {
        postValidator.validate(request);
        request.htmlContent = sanitizeHtml(request.htmlContent);
        request.summary = generateSummary(request.htmlContent);

        int result = mapper.insertOne(metadata, request);
        if (result < 0) {
            throw new ServerInternalException("insert article failed");
        }

        Objects.requireNonNull(request.returningId);

        return mapper.findOne(metadata, request.returningId)
                .orElseThrow(() -> new ServerInternalException("unwrap optional failed"));
    }

    public void deleteOne(int id) {
        mapper.deleteOne(metadata, id);
    }

    @Nonnull
    public Article updateOne(@Nonnull PutArticleRequest request) {
        putValidator.validate(request);

        if (request.htmlContent != null) {
            request.htmlContent = sanitizeHtml(request.htmlContent);
            request.summary = generateSummary(request.htmlContent);
        }

        mapper.updateOne(metadata, request);
        return mapper.findOne(metadata, request.id)
                .orElseThrow(() -> new ServerInternalException("unwrap optional failed"));
    }

    public Optional<Article> findOne(int id) {
        return mapper.findOne(metadata, id);
    }

    @Nonnull
    public Page<ArticleShortcut> findAll(@Nonnull ArticleQuery query) {
        List<ArticleShortcut> content = mapper.findAll(metadata, query);
        int totalPages = mapper.totalPages(metadata, query.size);

        return new Page<>(content, totalPages);
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
    private String sanitizeHtml(@Nonnull String unsafeHtml) {
        Safelist safelist = Safelist.relaxed()
                .removeTags("script")
                .removeTags("style");

        return Jsoup.clean(unsafeHtml, safelist);
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
