package com.steiner.vblog.model;

import jakarta.annotation.Nonnull;

public final class Tag {
    public int id;

    @Nonnull
    public String name;

    public int userId;

    public Tag(int id, @Nonnull String name, int userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }
}
