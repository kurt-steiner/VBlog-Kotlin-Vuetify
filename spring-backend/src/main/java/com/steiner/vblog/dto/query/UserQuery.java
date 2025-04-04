package com.steiner.vblog.dto.query;

import jakarta.annotation.Nullable;

@lombok.Builder
public class UserQuery {
    @Nullable
    public String name;

    @Nullable
    public String nickname;

    @Nullable
    public String email;

    @Nullable
    public String passwordHash;
}
