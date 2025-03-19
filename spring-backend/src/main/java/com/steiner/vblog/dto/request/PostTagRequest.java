package com.steiner.vblog.dto.request;

import jakarta.annotation.Nonnull;

public final class PostTagRequest implements IRequest {
    @Nonnull
    public String name;

    @Nonnull
    public Integer userId;

    public PostTagRequest() {
        this.name = null;
        this.userId = null;
    }

    public PostTagRequest(@Nonnull String name, @Nonnull Integer userId) {
        this.name = name;
        this.userId = userId;
    }
}
