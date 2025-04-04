package com.steiner.vblog.table_metadata;

import jakarta.annotation.Nonnull;

@lombok.Builder
public class ArticleTagMetadata {
    @Nonnull
    @lombok.NonNull
    public String tableName;

    @Nonnull
    @lombok.NonNull
    public String articlesTableName;

    @Nonnull
    @lombok.NonNull
    public String tagsTableName;
}
