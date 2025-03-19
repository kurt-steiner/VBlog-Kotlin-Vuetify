package com.steiner.vblog.table_metadata;

import jakarta.annotation.Nonnull;

public final class TagsMetadata {
    @Nonnull
    public String tableName;

    @Nonnull
    public String referenceTableName;

    @Nonnull
    public String associationTableName;

    public int nameLength;

    public TagsMetadata(@Nonnull String tableName, @Nonnull String referenceTableName, @Nonnull String associationTableName, int nameLength) {
        this.tableName = tableName;
        this.referenceTableName = referenceTableName;
        this.associationTableName = associationTableName;
        this.nameLength = nameLength;
    }
}
