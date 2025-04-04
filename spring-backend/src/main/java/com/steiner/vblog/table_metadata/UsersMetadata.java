package com.steiner.vblog.table_metadata;

import jakarta.annotation.Nonnull;

@lombok.Builder
public class UsersMetadata {
    @Nonnull
    @lombok.NonNull
    public String tableName;

    @Nonnull
    @lombok.NonNull
    public String imageItemsTableName;

    public int nameLength;
    public int nicknameLength;
    public int passwordHashLength;
    public int emailLength;
}
