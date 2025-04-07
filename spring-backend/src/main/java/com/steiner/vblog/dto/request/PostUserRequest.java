package com.steiner.vblog.dto.request;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class PostUserRequest implements IRequest {
    @Nullable
    public String returningId;

    @Nonnull
    public String name;

    @Nonnull
    public String passwordHash;

    @Nullable
    public String nickname;

    @Nullable
    public String email;

    @Nullable
    public Integer avatarId;

    public PostUserRequest() {
        this.returningId = null;
        this.name = null;
        this.passwordHash = null;
        this.nickname = null;
        this.email = null;
        this.avatarId = null;
    }

    public PostUserRequest(@Nonnull String name, @Nonnull String passwordHash, @Nullable String nickname, @Nullable String email, @Nullable Integer avatarId) {
        this.returningId = null;
        this.name = name;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.email = email;
        this.avatarId = avatarId;
    }
}
