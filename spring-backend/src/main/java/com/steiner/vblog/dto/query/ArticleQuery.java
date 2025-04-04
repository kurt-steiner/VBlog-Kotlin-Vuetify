package com.steiner.vblog.dto.query;

import com.steiner.vblog.model.article.ArticleSortBy;
import com.steiner.vblog.model.article.ArticleStatus;
import jakarta.annotation.Nullable;

@lombok.Builder
public class ArticleQuery {
    @Nullable
    public Integer authorId;

    @Nullable
    public ArticleStatus status;

    @Nullable
    public Integer tagId;

    @Nullable
    public Integer categoryId;

    @Nullable
    public String title;

    @Nullable
    public ArticleSortBy sortBy;

    public int page;
    public int size;
}
