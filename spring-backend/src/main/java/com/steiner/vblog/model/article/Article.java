package com.steiner.vblog.model.article;

import com.steiner.vblog.model.Category;
import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.User;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;

import java.util.List;

@lombok.Builder
@AllArgsConstructor
public class Article {
    public int id;

    @Nonnull
    @lombok.NonNull
    public String title;

    @Nonnull
    @lombok.NonNull
    public String markdownContent;

    @Nonnull
    @lombok.NonNull
    public String htmlContent;

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
