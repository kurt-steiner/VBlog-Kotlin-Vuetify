package com.steiner.vblog.model.article;

import com.steiner.vblog.model.Category;
import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.User;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.sql.Timestamp;
import java.util.List;

@lombok.Builder
public class ArticleShortcut {
    @Nonnull
    public static ArticleShortcut fromArticle(@Nonnull Article article) {
        return ArticleShortcut.builder()
                .id(article.id)
                .title(article.title)
                .summary(article.summary)
                .category(article.category)
                .author(article.author)
                .publishDate(article.publishDate)
                .editTime(article.editTime)
                .status(article.status)
                .tags(article.tags)
                .build();
    }

    public int id;

    @Nonnull
    @lombok.NonNull
    public String title;

    @Nonnull
    @lombok.NonNull
    public String summary;

    @Nullable
    public Category category;

    @Nonnull
    @lombok.NonNull
    public User author;

    @Nonnull
    @lombok.NonNull
    public java.sql.Timestamp publishDate;

    @Nonnull
    @lombok.NonNull
    public java.sql.Timestamp editTime;

    @Nonnull
    @lombok.NonNull
    public ArticleStatus status;

    @Nonnull
    @lombok.NonNull
    public List<Tag> tags;
}
