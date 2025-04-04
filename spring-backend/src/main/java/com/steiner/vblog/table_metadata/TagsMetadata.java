package com.steiner.vblog.table_metadata;

import jakarta.annotation.Nonnull;

@lombok.Builder
public class TagsMetadata {
    @Nonnull
    @lombok.NonNull
    public String tableName;

    @Nonnull
    @lombok.NonNull
    public String usersTableName;

    public int nameLength;
}
