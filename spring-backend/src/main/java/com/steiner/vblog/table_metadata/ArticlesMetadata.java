package com.steiner.vblog.table_metadata;

import jakarta.annotation.Nonnull;

public final class ArticlesMetadata {
    @Nonnull
    public String tableName;

    @Nonnull
    public String associationTableName;

    public int titleLength;
    public int defaultQueryPage;
    public int defaultQuerySize;

    public ArticlesMetadata(@Nonnull String tableName, @Nonnull String associationTableName, int titleLength, int defaultQueryPage, int defaultQuerySize) {
        this.tableName = tableName;
        this.associationTableName = associationTableName;
        this.titleLength = titleLength;
        this.defaultQueryPage = defaultQueryPage;
        this.defaultQuerySize = defaultQuerySize;
    }
}
