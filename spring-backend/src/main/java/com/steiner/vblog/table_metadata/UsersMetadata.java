package com.steiner.vblog.table_metadata;

import jakarta.annotation.Nonnull;

public final class UsersMetadata {
    @Nonnull
    public String tableName;

    @Nonnull
    public String associationTableName;

    public int nameLength;
    public int nicknameLength;
    public int passwordHashLength;
    public int emailLength;

    public UsersMetadata(@Nonnull String tableName, @Nonnull String associationTableName ,int nameLength, int nicknameLength, int passwordHashLength, int emailLength) {
        this.tableName = tableName;
        this.associationTableName = associationTableName;
        this.nameLength = nameLength;
        this.nicknameLength = nicknameLength;
        this.passwordHashLength = passwordHashLength;
        this.emailLength = emailLength;
    }
}
