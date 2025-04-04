package com.steiner.vblog.model.article;

import jakarta.annotation.Nonnull;

public class ArticleSortBy {
    public enum SortBy {
        ByTitle("title"),
        ByPublishDate("publish-date"),
        ByEditTime("edit-time");

        public final String field;
        SortBy(@Nonnull String field) {
            this.field = field;
        }
    };

    @Nonnull
    public final SortBy sortBy;
    public final boolean reverse;

    public ArticleSortBy(@Nonnull SortBy sortBy, boolean reverse) {
        this.sortBy = sortBy;
        this.reverse = reverse;
    }
}
