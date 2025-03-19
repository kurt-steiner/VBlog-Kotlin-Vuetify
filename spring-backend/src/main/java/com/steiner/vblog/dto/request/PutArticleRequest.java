package com.steiner.vblog.dto.request;

import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.article.ArticleStatus;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public final class PutArticleRequest {
    @Nonnull
    public Integer id;

    @Nullable
    public String title;

    @Nullable
    public String markdownContent;

    @Nullable
    public String htmlContent;

    @Nullable
    public Integer categoryId;

    @Nullable
    public List<Tag> tags;

    @Nullable
    public ArticleStatus status;

    public PutArticleRequest(
            @Nonnull Integer id,
            @Nullable String title,
            @Nullable String markdownContent,
            @Nullable String htmlContent,
            @Nullable Integer categoryId,
            @Nullable List<Tag> tags,
            @Nullable ArticleStatus status) {
        this.id = id;
        this.title = title;
        this.markdownContent = markdownContent;
        this.htmlContent = htmlContent;
        this.categoryId = categoryId;
        this.tags = tags;
        this.status = status;
    }
}
