package com.steiner.vblog.dto.request;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class PostTagRequest implements IRequest {
    @Nullable
    public Integer returningId;

    @Nonnull
    public String name;

    @Nonnull
    public Integer userId;

    public PostTagRequest() {
        this.returningId = null;
        this.name = null;
        this.userId = null;
    }

    public PostTagRequest(@Nonnull String name, @Nonnull Integer userId) {
        this.returningId = null;
        this.name = name;
        this.userId = userId;
    }
}
