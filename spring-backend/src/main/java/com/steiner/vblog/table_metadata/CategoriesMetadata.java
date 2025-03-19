package com.steiner.vblog.table_metadata;

import jakarta.annotation.Nonnull;

public final class CategoriesMetadata {
    @Nonnull
    public String tableName;

    @Nonnull
    public String referenceTableName;

    public int nameLength;

    public CategoriesMetadata(@Nonnull String tableName, @Nonnull String referenceTableName, int nameLength) {
        this.tableName = tableName;
        this.referenceTableName = referenceTableName;
        this.nameLength = nameLength;
    }
}
