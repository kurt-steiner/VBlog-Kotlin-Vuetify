package com.steiner.vblog.table_metadata;

import jakarta.annotation.Nonnull;

public final class ImageItemsMetadata {
    @Nonnull
    public String tableName;

    public int nameLength;
    public int pathLength;

    public ImageItemsMetadata(@Nonnull String tableName, int nameLength, int pathLength) {
        this.tableName = tableName;
        this.nameLength = nameLength;
        this.pathLength = pathLength;
    }
}
