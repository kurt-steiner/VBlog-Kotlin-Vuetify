package com.steiner.vblog.dto.request;

import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.article.ArticleStatus;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class PutArticleRequest implements IRequest {
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
    public String summary;

    @Nullable
    public List<Tag> tags;

    @Nullable
    public ArticleStatus status;

    public PutArticleRequest() {
        this.id = null;
        this.title = null;
        this.markdownContent = null;
        this.htmlContent = null;
        this.categoryId = null;
        this.summary = null;
        this.tags = null;
        this.status = null;
    }

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
        this.summary = null;
        this.tags = tags;
        this.status = status;
    }
}
