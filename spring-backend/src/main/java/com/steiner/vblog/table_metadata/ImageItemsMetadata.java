package com.steiner.vblog.table_metadata;

import jakarta.annotation.Nonnull;

@lombok.Builder
public class ImageItemsMetadata {
    @Nonnull
    @lombok.NonNull
    public String tableName;

    public int nameLength;
    public int pathLength;
}
