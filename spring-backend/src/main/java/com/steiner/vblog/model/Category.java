package com.steiner.vblog.model;

import jakarta.annotation.Nonnull;

import java.sql.Timestamp;

public final class Category {
    public int id;

    @Nonnull
    public String name;

    @Nonnull
    public java.sql.Timestamp createTime;

    public int userId;

    public Category(int id, @Nonnull String name, @Nonnull Timestamp createTime, int userId) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.userId = userId;
    }
}
