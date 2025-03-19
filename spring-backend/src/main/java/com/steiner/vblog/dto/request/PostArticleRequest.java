package com.steiner.vblog.dto.request;

import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.article.ArticleStatus;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public final class PostArticleRequest implements IRequest {
    @Nonnull
    public String title;

    @Nonnull
    public String markdownContent;

    @Nonnull
    public String htmlContent;

    @Nullable
    public Integer categoryId;

    @Nullable
    public List<Tag> tags;

    @Nonnull
    public ArticleStatus status;

    @Nonnull
    public Integer authorId;

    public PostArticleRequest() {
        this.title = null;
        this.markdownContent = null;
        this.htmlContent = null;
        this.categoryId = null;
        this.tags = null;
        this.status = null;
        this.authorId = null;
    }

    public PostArticleRequest(
            @Nonnull String title,
            @Nonnull String markdownContent,
            @Nonnull String htmlContent,
            @Nullable Integer categoryId,
            @Nullable List<Tag> tags,
            @Nonnull ArticleStatus status,
            @Nonnull Integer authorId) {
        this.title = title;
        this.markdownContent = markdownContent;
        this.htmlContent = htmlContent;
        this.categoryId = categoryId;
        this.tags = tags;
        this.status = status;
        this.authorId = authorId;
    }
}
