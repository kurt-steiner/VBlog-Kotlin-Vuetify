package com.steiner.vblog.model.article;

import com.steiner.vblog.model.Category;
import com.steiner.vblog.model.Tag;
import com.steiner.vblog.model.User;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.sql.Timestamp;
import java.util.List;

public final class ArticleShortcut {
    @Nonnull
    public static ArticleShortcut fromArticle(@Nonnull Article article) {
        return new ArticleShortcut(
                article.id,
                article.title,
                article.summary,
                article.category,
                article.author,
                article.publishDate,
                article.editTime,
                article.status,
                article.tags
        );
    }


    public int id;

    @Nonnull
    public String title;

    @Nonnull
    public String summary;

    @Nullable
    public Category category;

    @Nonnull
    public User author;

    @Nonnull
    public java.sql.Timestamp publishDate;

    @Nonnull
    public java.sql.Timestamp editTime;

    @Nonnull
    public ArticleStatus status;

    @Nonnull
    public List<Tag> tags;

    private ArticleShortcut(int id, @Nonnull String title, @Nonnull String summary, @Nullable Category category, @Nonnull User author, @Nonnull Timestamp publishDate, @Nonnull Timestamp editTime, @Nonnull ArticleStatus status, @Nonnull List<Tag> tags) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.category = category;
        this.author = author;
        this.publishDate = publishDate;
        this.editTime = editTime;
        this.status = status;
        this.tags = tags;
    }
}
