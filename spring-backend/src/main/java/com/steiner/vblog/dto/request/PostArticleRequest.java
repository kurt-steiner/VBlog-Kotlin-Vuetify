package com.steiner.vblog.dto.request;

import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.article.ArticleStatus;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.NonNull;

import java.util.List;

public class PostArticleRequest implements IRequest {
    @Nullable
    public Integer returningId;

    @Nonnull
    public String title;

    @Nonnull
    public String markdownContent;

    @Nonnull
    public String htmlContent;

    @Nullable
    public Integer categoryId;

    @Nullable
    public String summary;

    @Nullable
    public List<Tag> tags;

    @Nonnull
    public ArticleStatus status;

    @Nonnull
    public Integer authorId;

    public PostArticleRequest() {
        this.returningId = null;
        this.title = null;
        this.markdownContent = null;
        this.htmlContent = null;
        this.categoryId = null;
        this.summary = null;
        this.tags = null;
        this.status = null;
        this.authorId = null;
    }

    public PostArticleRequest(@NonNull String title, @NonNull String markdownContent, @NonNull String htmlContent, @Nullable Integer categoryId, @Nullable List<Tag> tags, @NonNull ArticleStatus status, @NonNull Integer authorId) {
        this.returningId = null;
        this.title = title;
        this.markdownContent = markdownContent;
        this.htmlContent = htmlContent;
        this.categoryId = categoryId;
        this.summary = null;
        this.tags = tags;
        this.status = status;
        this.authorId = authorId;
    }
}
