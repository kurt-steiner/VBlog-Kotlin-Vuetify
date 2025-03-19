package com.steiner.vblog.dto.query;

import jakarta.annotation.Nullable;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public final class UserQuery {
    @Nullable
    public String name;

    @Nullable
    public String nickname;

    @Nullable
    public String email;

    @Nullable
    public String passwordHash;
}
