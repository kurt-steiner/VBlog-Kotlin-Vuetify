package com.steiner.vblog.table_metadata;

import jakarta.annotation.Nonnull;

@lombok.Builder
public class ArticlesMetadata {
    @Nonnull
    @lombok.NonNull
    public String tableName;

    @Nonnull
    @lombok.NonNull
    public String articleTagTableName;

    @Nonnull
    @lombok.NonNull
    public String usersTableName;

    @Nonnull
    @lombok.NonNull
    public String categoriesTableName;

    @Nonnull
    @lombok.NonNull
    public String tagsTableName;

    public int titleLength;
    public int summaryLength;
    public int defaultQueryPage;
    public int defaultQuerySize;
}
