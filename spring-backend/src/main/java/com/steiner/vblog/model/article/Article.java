package com.steiner.vblog.model.article;

import com.steiner.vblog.model.Category;
import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.User;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Article {
    public int id;

    @NonNull
    @Nonnull
    public String title;

    @NonNull
    @Nonnull
    public String markdownContent;

    @NonNull
    @Nonnull
    public String htmlContent;

    @Nonnull
    public String summary;

    @Nullable
    public Category category;

    @NotNull
    @Nonnull
    public User author;

    @NotNull
    @Nonnull
    public java.sql.Timestamp publishDate;

    @NotNull
    @Nonnull
    public java.sql.Timestamp editTime;

    @NotNull
    @Nonnull
    public ArticleStatus status;

    @NotNull
    @Nonnull
    public List<Tag> tags;
}
