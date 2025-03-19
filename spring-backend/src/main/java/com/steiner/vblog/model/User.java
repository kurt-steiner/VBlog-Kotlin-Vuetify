package com.steiner.vblog.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.sql.Timestamp;

public final class User {
    public int id;

    @Nonnull
    public String name;

    @Nullable
    public String nickname;

    @Nullable
    public String email;

    @Nonnull
    public java.sql.Timestamp registerTime;

    @Nullable
    public ImageItem avatar;

    public User(int id, @Nonnull String name, @Nullable String nickname, @Nullable String email, @Nonnull Timestamp registerTime, @Nullable ImageItem avatar) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.registerTime = registerTime;
        this.avatar = avatar;
    }
}
