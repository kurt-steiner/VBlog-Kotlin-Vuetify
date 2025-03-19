package com.steiner.vblog.dto.request;

import jakarta.annotation.Nonnull;

public final class PostCategoryRequest implements IRequest {
    @Nonnull
    public String name;

    @Nonnull
    public Integer userId;

    public PostCategoryRequest() {
        this.name = null;
        this.userId = null;
    }

    public PostCategoryRequest(@Nonnull String name, @Nonnull Integer userId) {
        this.name = name;
        this.userId = userId;
    }
}
