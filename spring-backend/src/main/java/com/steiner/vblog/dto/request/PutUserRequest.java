package com.steiner.vblog.dto.request;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class PutUserRequest implements IRequest {
    @Nonnull
    public Integer id;

    @Nullable
    public String name;

    @Nullable
    public String nickname;

    @Nullable
    public String passwordHash;

    @Nullable
    public String email;

    @Nullable
    public Integer avatarId;

    public PutUserRequest() {
        this.id = null;
        this.name = null;
         this.nickname = null;
         this.passwordHash = null;
         this.email = null;
         this.avatarId = null;
    }

    public PutUserRequest(@Nonnull Integer id, @Nullable String name, @Nullable String nickname, @Nullable String passwordHash, @Nullable String email, @Nullable Integer avatarId) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.passwordHash = passwordHash;
        this.email = email;
        this.avatarId = avatarId;
    }
}
